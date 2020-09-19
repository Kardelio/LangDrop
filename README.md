## Lang Drop
Lang Drop is a very small android word translation game for one player.

When the game begins the player will be presented with a static word on the screen and an
additional "Falling" word that slowly plumbits from the top for the screen to the bottom.

The player must try to decide if the falling word is an accurate translation of the
above static word before the word hits the bottom of the screen!

The player will have X amount of lives and will lose one on an incorrect answer or
if the falling word hits the deck before an answer is submitted!

### TODO
* Screenshot here...
* Logo here? (NTH)

----

### Design & Development Decisions

#### Single Activity
The app will utilize the now recommedend single activity pattern, where the "screens" in the app
will simply be fragments that the host single activity swaps in and out. To achieve this the app
will utilize googles Jetpack Navigation component.

#### MVVM
The app will be architectured using the MVVM clean architecture pattern, where by views will only
perform and do *view* things, the views will observe for data in their respective viewmodels, which in
turn will be populate by data from repositories.

#### Simple Data from file
The app will not communicate with a remote server so there is no need to implement any HTTP client (aka retrofit).
Instead the data for the words that will make up the game has been provided in a json file (below)

    /res/raw/words_v2.json

Currently this file contains english to spanish translations, however in the future this game will be
made easily extensible to include other language packs.

#### Selected Libs
Below is a list of some of the important libs/frameworks etc that I have selected for this project

* Dagger Hilt for Dependency Injection
* GSON for JSON Parsing
* Jetpack Nav Component for Navigation between fragments

#### Tests
Ofcourse this app will attempt to be covered by quality unit and integration tests, which ofcourse can be
found in these files (respectively):

    /app/test
    /app/androidTest

#### VCS git
I will be utilizing git as my Version Control System. However as I am the only developer on this project I will most
likely not be using the 'Git Flow' approach to branching and instead I will perform the age old sin of pushing changes to  
master. Again this is simply because I am working alone on this project for now and in normal circumstances I would use a  
smart and effective branching strategy to implement new features and bug fixes.

----

### Development Log (per commits)

Just going to write my approach, what I have done and thoughts here as I progress.
I will be gently working on and off on this codebase over the following few days so commits
may be slightly sporadic.

#### Commit 1
For the Initial commit I have done the following:
* Setup the initial project in AS
* Updated the .gitignore file
* Layed out my initial thoughts for the project sturcutre in terms of packages, folders and screens:
  - Menu / Home screen ?
  - Game Screen
  - Score Screen ?
* I have brought in a collection of dependencies that I foresee will be utilized, these include:
  - Dagger Hilt for dependency injection
  - Jetpack Navigation Component and Safe Args for navigating between fragments and visual nav_graph
  - GSON for JSON parsing
  - Some KTX dependencies to utilize an array of brilliant KTX functions (e.g. by viewModels())
* I have setup briefly the word repository and made sure the JSON file reads in correctly
* Investigated briefly the falling word with a motion layout, it works nicely
* I quickly added a random number generator with an interface for testing later
* I have setup up an empty gamefragment and wired up the injection with a viewmodel and connection to the word repo
* I have written this README file

#### Commit 2
TODO
