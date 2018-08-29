/************************************************************
Basic instructions:

We have submitted two APK files: Traveler.apk and Traveler-unaligned.apk
The instructions for APK submitting on the website say to commit the unaligned one (unsigned) but we have
committed both an unsigned one and a signed one with the key file. 

The signed one is Traveler.apk and we recommend that you use it.

You don't have to push any files to your phone or onto the emulator. The APP is designed that will assign
the very FIRST user who register as administrator. Then this user will have the ability to change other users into administrators.
All information is stored in one file (passwords.txt) as well as on SQLite Databases (also passwords.txt).

The edit user info tab will accept changes without touching the actual password. i.e. you can change everything without changing the password.
There's also restrictions on the changing of the email, the new email MUST NOT be presented in the database (i.e. it must not be used by any other user). 
And once the email is changed, the user have to use the new email to login.

You cannot change a user to Guest nor can you make a Guest user, for obvious reasons (Guests cannot have any login information).
Searching travels will show travels that have 0 seats left (so you can edit them if you're an admin)
However, obviously, itineraries will not show any with 0 seats.

After uploading a user file to the database, the users will not have passwords set for them. However, if someone logs in with
one of the new emails, they can login with any password, and the password they login with will be set as their account password.

For editing travels, similarly, you also cannot change the unique number and travel type to something that already exists.
Editing travels in any way will also unbook any itineraries containing that travel, from all users, for safety.

An administrator can book or unbook an itinerary for another user by first searching the user, then clicking 'Book Itinerary', then searching
for the itinerary, then clicking Book or Unbook.

After editing a travel or a user, the search list is not updated until you search again (click the search button again). 
However, itinerary lists will always be updated.


/************************************************************
Troubleshooting:

Files are stored in the default internal storage location: /data/data/b07.project/files
If you are having any trouble, a sample passwords.txt has been provided. It is not recommended that you use it unless you really need to.
Push it to the directory above and you can login as admin using the credentials: (Email) johntitor@fg.com (Password) johnnn

A sample passwords_sql.txt is also provided. You may rename this to passwords.txt and place it in /data/data/b07.project/databases
This is the last resort; only do this if the above passwords.txt file does not work. The same data is in both files.

Input paths to files are relative to the location: /data/data/b07.project/app_record_data/
(i.e. for uploading data to the app)

Once you open the app, it is possible that you will see some buttons that are stacked together, and the text is unreadable. 
If this happens, please scale down the font size of your phone.

/************************************************************
For the bonus mark, we have done the following:

1) Extra features:
   a) Ability to unbook an itinerary
   b) Sort single travels as well as itineraries
   c) Can add new users and travels by using the UI (admin only)
   d) Password recovery option - generates a new password for you
   e) Password edit (for yourself, or for others too if you're an admin) by using the UI
   f) Ability to search users by their name or their email, or both (admin only)
   g) Ability to search travels with only certain fields (e.g. only entering a date, or only entering an origin, or entering nothing)
   h) Multiple ways to sort (start time, end time, descending order, and more)
   i) More user types - Moderators (can add and edit travel information only, not user information) and guest users (can search only), easy addition of new ones
   j) Multiple travel types - Railroads, Ferries, and others, with easy addition of new ones

2) Backend: modular design with:
   a) The appropriate usage of enums, generics and interfaces and packaging
   b) Recursive, simple itinerary search algorithm
   c) Time efficient storage of itineraries and databases, using the appropriate Collections
   d) Memory efficient app design, using only one reference of each object in serialization
   e) Encrypted data for storage, and separate hashes for passwords (never stored in plaintext)

3) Frontend: additional code for:
   a) A better interface with dark theme and a startup screen
   b) Gesture tracking, listeners for user input, refreshing activities, and appropriate usage of the Activity life cycle
   c) Proper use of Intents and back-end application contexts, keeping serialization of objects only to when it is needed
   d) Clever use of Intents and Fragments to minimize the amount of separate layouts, for easier modular design of layouts
   e) Protection against app killing and resource failure; use of background services and saving the session prevents any loss of data
   f) Usage of both SQLite database and file storage (in case of data corruption if it ever happens)
   g) Usage of SharedPreferences (Android app preferences) for login

************************************************************/