package mscope.controller

import mscope.datamodel.File
import mscope.datamodel.Directory
import java.nio.file.Paths

/**
 * File explorer loads and keeps track of file and directory elements. The explorer can be used 
 * for traversing within the file system
 */
class FileExplorer(private var rootPath: PathData = new PathData(Paths.get("")))
{
    // INITIAL CODE    ---------------
    
    // The initial path must lead to a directory
    if (!rootPath.isDirectory)
        rootPath = rootPath.parentPath.map(new PathData(_)).getOrElse(rootPath)
    
    
    // ATTRIBUTES    -----------------
    
    // Current directory (A vector starting with the root directory and going down the current path)
    private var currentDirPath = Vector(pathToDirectory(rootPath))
    
    
    // COMPUTED PROPERTIES    --------
    
    /**
     * The currently explored directory
     */
    def currentDirectory = currentDirPath.last
    
    
    // OTHER METHODS    --------------
    
    /**
     * Moves down to a subdirectory with the provided name
     * @return whether the directory was changed
     */
    def moveDown(dirName: String) = 
    {
        val nextDirectory = currentDirectory.children.find(_.name == dirName)
        if (nextDirectory.isDefined)
        {
            currentDirPath :+ nextDirectory.get
            true
        }
        else
        {
            false
        }
    }
    
    /**
     * Moves up to the parent directory
     * @return whether the directory was changed
     */
    def moveUp() = 
    {
        if (currentDirPath.size > 1)
        {
            currentDirPath = currentDirPath.dropRight(1)
            true
        }
        else
        {
            // May have to move the root path upwards and explore the sibling directories
            val parentPath = rootPath.parentPath.map(new PathData(_))
            if (parentPath.isDefined)
            {
                // Doesn't parse the current directory again, only siblings
                val siblingPaths = parentPath.get.childPaths.toOption.getOrElse(Vector()).map(
                        new PathData(_));
                val siblingFiles = siblingPaths.filter(_.isFile).map(pathToFile)
                val siblingDirectories = siblingPaths.filter(p => 
                        p.isDirectory && p.fileName != currentDirectory.name).map(pathToDirectory);
                
                val parentDirectory = new Directory(parentPath.get.fileName, siblingFiles, 
                        siblingDirectories :+ currentDirectory)
                
                rootPath = parentPath.get
                currentDirPath = Vector(parentDirectory)
                true
            }
            else
            {
                false
            }
        }
    }
    
    
    // Only works for paths that lead to directories
    private def pathToDirectory(path: PathData): Directory = 
    {
        val childPaths = path.childPaths.toOption.map(_.map(new PathData(_))).getOrElse(Vector())
        val childFiles = childPaths.filter(_.isFile).map(pathToFile)
        val childDirectories = childPaths.filter(_.isDirectory).map(pathToDirectory)
        
        new Directory(path.fileName, childFiles, childDirectories)
    }
    
    // Only works for paths that lead to files
    private def pathToFile(path: PathData) = new File(path.fileName, path.size.getOrElse(0))
}