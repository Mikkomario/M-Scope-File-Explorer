package mscope.controller

import collection.JavaConverters._
import utopia.flow.util.NullSafe._

import java.nio.file.Path
import java.nio.file.Files
import java.nio.file.LinkOption
import scala.util.Try
import scala.util.Success
import java.util.stream.Collectors

/**
 * Path data encompasses data related to a certain file path
 * @author Mikko Hilpinen
 * @since 3.1.2018 - v0.1
 */
class PathData(val path: Path)
{
    // ATTRIBUTES    -----------------------
    
    /**
     * The name of the file denoted by the path
     */
    lazy val fileName = path.getFileName.toOption.map(_.toString).getOrElse("~")
    
    /**
     * Whether the path represents a directory
     */
    lazy val isDirectory = Try(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)).toOption.getOrElse(false)
    
    /**
     * The size of the file at the end of the path. None if the path doesn't lead to a regular file
     */
    lazy val size = if (isFile) Try(Files.size(path)).toOption else None
    
    /**
     * The path leading to the parent directory. None if this path doesn't have a parent directory
     */
    lazy val parentPath = path.toAbsolutePath().getParent.toOption
    
    
    // COMPUTED PROPERTIES    -------------
    
    /**
     * Whether the path represents a regular file
     */
    def isFile = !isDirectory && exists
    
    /**
     * Whether the path leads to any file or directory
     */
    def exists = Try(Files.exists(path, LinkOption.NOFOLLOW_LINKS)).toOption.getOrElse(false)
    
    /**
     * The paths to elements directly below this path. May fail.
     */
    def childPaths = 
    {
        if (isDirectory) 
            Try(Files.list(path).collect(Collectors.toList()).asScala.toVector)
        else
            Success(Vector())
    }
    
    
    // OTHER METHODS    ------------------
    
    /**
     * Checks whether this path contains data from the provided path
     */
    def represents(path: Path) = this.path.equals(path)
}