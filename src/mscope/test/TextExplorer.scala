package mscope.test

import mscope.controller.FileExplorer
import mscope.datamodel.DataBlob

/**
 * This app offers a simple command line interface for file exploration
 * @author Mikko Hilpinen
 * @since 3.1.2018 - v0.1
 */
object TextExplorer extends App
{
    val megaMod = 1.0 / (1024 * 1024)
    
    // Sets up the explorer
    val explorer = new FileExplorer()
    
    // TODO: Describe gigs and kilos too
    def desc(data: DataBlob) = f"\n- ${ data.name }\t${ data.size * megaMod }%.1f Mb"
    def list(items: Seq[DataBlob], title: String) = 
    {
        // Describe directories asynchronously, updaring the lines when data is read 
        // (Reading the whole drive may take a serious amount of time)
        if (!items.isEmpty)
        {
            val hiddenAndOther = items.groupBy(_.name.startsWith(".")).mapValues(_.sortWith(_.size > _.size))
            val sortedItems = hiddenAndOther.getOrElse(true, Vector()) ++ hiddenAndOther.getOrElse(false, Vector())
            
            println(sortedItems.foldLeft(title)(_ + desc(_)))
        }
    }
    
    def ls() = 
    {
        println()
        list(explorer.currentDirectory.children, "Directories")
        list(explorer.currentDirectory.files, "Files")
        println()
    }
    
    // Reads the user input until an empty line is returned
    ls()
    io.Source.stdin.getLines().takeWhile(!_.isEmpty()).foreach(input => 
    {
        if (input == "..")
        {
            if (explorer.moveUp())
            {
                ls()
            }
            else
            {
                println("No parent directory")
            }
        }
        else
        {
            if (explorer.moveDown(input))
            {
                ls()
            }
            else
            {
                println("No such directory")
            }
        }
        
        print("$ " + s"${ explorer.currentDirectory.name }: ")
    })
    
    println("Bye!")
}