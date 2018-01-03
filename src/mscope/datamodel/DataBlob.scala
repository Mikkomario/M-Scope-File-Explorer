package mscope.datamodel

/**
 * A data blob can represent any piece of data (bytes)
 * @author Mikko Hilpinen
 * @since 3.1.2018
 */
trait DataBlob
{
    // ABSTRACT    ---------------------
    
    /**
     * The size of the blob (in bytes)
     */
    def size: Long
    
    /**
     * The name of the item represented by the data blob
     */
    def name: String
}