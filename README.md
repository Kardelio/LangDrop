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
For the second commit I have been working on ensuring that the actual game mechanics work.
I have been testing the Motionlayout for the "falling word" solution and it is a great solution.
It is very clean and concise and because I can fully control the motionlayout in code and also
make nice animations very easily this is a great choice for the actual falling word mechanic.

I have setup the gameFragment UI in a very basic way, I will swing back to this UI to make it
look better at the end if time permits. However more importantly I setup the GameViewModel, which
will be responsible for the logic of the UI state. I have set up a GameState livedata that has currently
3 states: `Active` for when the player is playing the game, `Over` for when the game is over and the player has
run out of lives and `Pre` which is essentially the state before the game starts for the first time
where the user chooses to play the game or exit the app. The relationship and transition between
these UI states based on User interaction and running out of lives works and works well.

I added a "pre" game menu screen (sort of) into the motionlayout where the user can choose to play
the game or exit the app. Clicking the play button begins the game and the words to drop.

I have setup very quickly some logic for the Word Repo, which reads in the JSON file to return to me
a random word pairing, with a arbitery 50/50 chance the user will see the `correct` word pairing. I have
also passed the "answer" if the pairing is right or wrong down with the Word pair object so the VM can
quickly find out if the words fit together.

I will now continue on and add the scores data and lives data to the UI. And then ensure that the user
"Correct" and "Wrong" buttons are all wired up in order for the game to be playable. I will then start to
write some tests for the VM, Repo and Fragment passing in mocked data to ensure the game logic works as
it should and build confidence when I continue to change the project...

#### Commit 3
This is a very simple commit. I have done the following:

* Updated the game fragment UI to include score and lives (number form atm)
* Made sure the user can use the correct and wrong buttons and the data is sent to VM
* score and lives are updated depending on the game logic now
* User can hit a game over state when the game is over due to lack of lives

So everything in the game is working. However now it just looks gross as there has been no
love in terms of UI or UX. Also I need to write some tests for the logic. I will write these
tests now before I make the game itself look and feel better.

#### Commit 4
I have created unit tests for the GameViewModel and the WordRepository.
Both are very simple and test some of the important app logic paths to ensure that the results
are as expected based on mocked input.

As part of this commit I have added the mockito dependency and also built a sharedTest folder
that will contain files that are useful for both the unit tests and the instrumentation tests
this sharedTest folder has been included in the build.gradle source sets and as of now contains
one utility file that contains several useful extension functions for when it comes to writing mocks,
capturing the values within livedata objects and stubbing mocked data object (whenever).

I am not yet finished writing all the test cases and will continue to write more tests as I continue, however
for now I want to go back to the UI and make the appearance nicer to look at and then write some Instrumentation
tests to go along witht the UI. I also need to do the Game over screen.
