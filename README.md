# CIS2430*S20 (Object-Oriented Programming) Assignment 2

A game modeled after the 1977 game Colossal Caves, developed using OOP principles with Java.

## Compiling and running

`mvn clean compile checkstyle:checkstyle test exec:java -Dexec.args="[flag] [filename]" assembly:single`

* you may use either of the command-line switches `-a` and `-l`
  * `-a` followed by a valid JSON filename will load a new adventure with the setting and rooms described in your file
    * If the file is within another directory (outside of root), you must use:
      * `../filename.json` to navigate up one directory level, or
      * `/foldername/filename.json` to enter another folder.
    * a sample JSON file `custom_adventure.json` is provided in the root directory for use :)
  * `-l` followed by a valid game save name will load a previously saved game
  * using either of the flags is optional; no flag will load the default adventure

## Game play

* use 'go' and the subjects: N, S, E, W, up, or down to move from room to room in the loaded adventure.
* type 'look' to see a longer description of the room you are in.
  * type 'look' followed by the name of an item to see a longer description of an item in the room you are in.
* type 'take' followed by an item name to pick up an item and carry it.
* type 'inventory' to see list of items in your inventory.
* enter 'quit' at any point to quit.
* enter 'help' at any point during the game to for commands/help.
* upon choosing to quit, you will be given a choice to save your progress in the game:
  * enter a name to identify the game save
  * load your saved game when starting the program if desired
