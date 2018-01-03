package mscope.datamodel

/**
 * A directory may contain multiple files and / or other directories
 * @author Mikko Hilpinen
 * @since 3.1.2018
 */
class Directory(val name: String, val files: Vector[File], val children: Vector[Directory]) extends DataBlob
{
    // ATTRIBUTES    -----------------------------
    
    lazy val size = elements.foldLeft(0: Long)(_ + _.size)
    
    
    // COMPUTED PROPERTIES    --------------------
    
    private def elements = files ++ children
}