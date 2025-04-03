---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# HealthSync User Guide

---

HealthSync is a **desktop application tailored for healthcare administrators in family clinics.** It centralises patients' personal information and emergency contact details into a single, easily accessible database, enabling administrators to efficiently manage data and contacts, even in time-sensitive situations.

By combining the speed of a Command Line Interface (CLI) with the visual clarity of a Graphical User Interface (GUI), HealthSync optimizes workflow for fast-typing administrators, enabling them to retrieve critical patient details and establish contact more efficiently than traditional GUI-only applications.

This ensures rapid response when every second matters.

---

**⚠️ Warning:** HealthSync is only designed for **Singapore-based family clinics**. It operates exclusively in **English** and does not support other languages or international clinic formats.

Using HealthSync with other languages or across multiple countries and timezones may lead to unexpected behaviour.

---
## Table of Contents
1. [How to use this User Guide](#how-to-use-this-user-guide)
2. [Quick Start](#quick-start)
3. [Overview of GUI](#overview-of-gui)
4. [Features](#features)
    - [Viewing help: `help`](#viewing-help-help)
    - [Adding a patient: `add`](#adding-a-patient-add)
    - [Schedule an appointment: `schedule`](#scheduling-an-appointment-schedule)
    - [Listing all patients: `list`](#listing-all-patients-list)
    - [Sorting patients: `sort`](#sorting-patients-sort)
    - [Editing a patient: `edit`](#editing-a-patient-edit)
    - [Setting emergency contact: `emergency`](#setting-emergency-contact-emergency)
    - [Locating patients by name: `find`](#locating-patients-by-name-find)
    - [Archiving a patient: `archive`](#archiving-a-patient-archive)
    - [Listing archived patients: `listarchive`](#listing-archived-patients-listarchive)
    - [Unarchiving a patient: `unarchive`](#unarchiving-a-patient-unarchive)
    - [Deleting a patient: `delete`](#deleting-a-patient-delete)
    - [Clearing all entries: `clear`](#clearing-all-entries-clear)
    - [Tag Management](#tag-management)
        - [Adding a tag: `tag`](#adding-a-tag-tag)
        - [Deleting a tag: `tag`](#deleting-a-tag-tag)
    - [Undoing a command: `undo`](#undoing-a-command-undo)
    - [Redoing a command: `redo`](#redoing-a-command-redo)
    - [Exiting the program: `exit`](#exiting-the-program-exit)
    - [Saving the data](#saving-the-data)
    - [Editing the data file](#editing-the-data-file)
5. [FAQ](#faq)
6. [Known Issues](#known-issues)
7. [Glossary](#glossary)
   - [Terminology](#terminology)
   - [Valid inputs for patient parameters](#valid-inputs)
5. [Command Summary](#command-summary)


--------------------------------------------------------------------------------------------------------------------
## How to use this User Guide
This User Guide is designed to help you understand and use HealthSync effectively. Below are some tips on how to navigate and use this guide:

1. **[Table of Contents](#table-of-contents)**: Use this to instantly jump to the section you are interested in.
2. **[Quick Start](#quick-start)**: Step-by-step instructions for first-time users.
3. **[Overview of GUI](#overview-of-gui)**: Familiarise yourself with HealthSync's interface.
4. **[Features](#features)**: Detailed explanations of all commands with formats, parameters and examples.
5. **[Command Summary](#command-summary)**: A quick reference table for command formats.
6. **[FAQ](#faq)**: Answers to common questions and troubleshooting tips.
7. **[Known Issues](#known-issues)**: Lists existing issues and their solutions.
8. **[Glossary](#glossary)**: Definitions of key terms used in this guide.
9. **[Notes and Tips](#notes-and-tips)**: Helpful insights highlighted throughout this the guide.

By referring to these sections, you can quickly find the information you need and fully utilize HealthSync.

## Legend
**⚠️**: The symbol serves as a warning that executing a specific command in a given context may result in unexpected behavior.
<box type="tip">: The symbol provides helpful guidance on using the command effectively to meet your needs.

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   * You can check your Java version by following the instructions [here](https://www.wikihow.com/Check-Your-Java-Version-in-the-Windows-Command-Line).
   * If you do not have Java `17` or above installed in your computer, you can download Java from [here](https://www.oracle.com/java/technologies/downloads/#java17).
   * **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

2. Download the latest release of the `healthsync.jar` file from [here](https://github.com/AY2425S2-CS2103T-F11-3/tp/releases).

3. Copy the file to the folder you want to use as the _home folder_ for HealthSync. The _home folder_ will be where all the data files will be saved.

4. For *Windows:* Open the home folder and right-click anywhere in the blue box, as shown in the image below. Click "Open in Terminal". A terminal window will pop up, then type in the command `java -jar medconnect.jar` to run the application.
   ![Windows.png](images/Windows.png)

   For *MacOS:* Right-click home folder. Hover over "Services". Select "New Terminal at folder". A terminal window will pop up, then type in the command `java -jar healthsync.jar` to run the application.

   <img src="images/MacUser.png" width="330" height="220">

5. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all patients.
   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a patient named `John Doe` to HealthSync.
   * `emergency 1 n/John Smith p/98765432 r/Father` : Sets an emergency contact for the 1st patient.
   * `delete 3` : Deletes the 3rd patient shown in the current list.
   * `clear` : Deletes all patients.
   * `exit` : Exits the app.

6. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------
## Overview of [GUI](#gui)
HealthSync features a clean and intuitive graphical user interface (GUI) designed to help users efficiently manage patient records and appointments.
The main interface consists of several key components:

<img src="images/UpdatedHealthSync.png" width="330" height="220">

1. **Menu Bar** - Provide quick access to essential functions:
   * **File:**
     * Exit: Closes the application safely.
   * **Help:** Opens a link to the HealthSync User Guide, providing instructions on how to use the application. <img src="images/HelpMenu.png" width="300" height="200">
2. **Command Box** - 
   * **Help:** Opens a link to the HealthSync User Guide, providing instructions on how to use the application. ![HelpMenu.png](images/HelpMenu.png)
   * Users can enter text-based commands to interact with the application.
3. **Command Output Box**
   * Display messages in response to user commands.
   * Provides feedback such as confirmations, errors, and system notifications.
4. **Patient List Panel**
   * Displays a list of all registered patients.
   * Clicking on a patient will show their details in the Person Detail Panel
5. **Person Details Panel**
   * Shows detailed information about the selected patient, such as their medical history, emergency contacts, and insurance details.
6. **Tags**
    * Used to record Allergies, Conditions and Insurance of a patient.
    * Each type of tag is represented by a different color for easy identification:
      * Red: Allergy
      * Green: Condition
      * Blue: Insurance

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Viewing help : `help`

Shows a message explaining how to access the help page.

<img src="images/HealthSyncHelpMessage.png" width="250" height="25">

Format: `help`

### Adding a patient: `add`

Adds a patient to HealthSync.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS`

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567`

**⚠️ Warning:** An entry is considered a **duplicate** if it has the same name and phone number or the same name and email address.

### Scheduling an appointment: `schedule`

Schedules an appointment for a patient in HealthSync.

**Format:**  
`schedule INDEX dd-MM-yyyy HH:mm`

<box type="tip" seamless>  
Ensure the date and time are in the future.  
</box>

**Examples:**
- `schedule 1 12-04-2025 14:30`
- `schedule 2 05-06-2025 09:00`

**⚠️ Warning:** An appointment is considered a **duplicate** if it has the same date and time as an existing appointment.

### Listing all patients : `list`

Shows a list of all patients in HealthSync.

Format: `list`

### Sorting patients : `sort`

Sorts the list of patients by a specified field.

Format: `sort FIELD`

* Sorts the patient list by the specified `FIELD`.
* Available fields: `name`, `appointment`
* The sorting is case-insensitive.

Examples:
* `sort name` Sorts patients in ascending alphabetical order by name.
* `sort appointment` Sorts patients by appointment date in lexicographical order, with the nearest upcoming appointment listed first.

  <img src="images/SortExample.png" width="300" height="200">

### Editing a patient : `edit`

Edits an existing patient in HealthSync.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS]`

* Edits the patient at the specified `INDEX`. The index refers to the index number shown in the displayed patient list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st patient to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower` Edits the name of the 2nd patient to be `Betsy Crower`.

### Setting Emergency Contact : `emergency`

Sets or updates the emergency contact for a patient in HealthSync.

Format: `emergency INDEX n/NAME p/PHONE_NUMBER r/RELATIONSHIP`

* Sets the emergency contact for the patient at the specified `INDEX`.
* The index refers to the index number shown in the displayed patient list.
* The index **must be a positive integer** 1, 2, 3, …
* All fields (name, phone, relationship) are required.

Examples:
* `emergency 1 n/Alden Tan p/98765432 r/Boyfriend` Sets the emergency contact for the 1st patient to be Alden Tan (Boyfriend) with phone number 98765432.
* `emergency 2 n/Mary Goh p/88761432 r/Mother` Sets the emergency contact for the 2nd patient to be Mary Goh (Mother) with phone number 88761432.


### Locating patients by name: `find`

Finds patients whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Name, Phone number and Email are searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Patients matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find david roy` returns `David Li`, `Roy Balakrishnan`<br>
  <img src="images/HealthSyncFindResult.png" width="240" height="300">


### Archiving a patient : `archive`

Archives the specified patient from HealthSync. Removes patient from address book and adds them into archive list.

Format: `archive INDEX`

* Archives the patient at the specified `INDEX`.
* The index refers to the index number shown in the displayed patient list.
* The index **must be a positive integer** 1, 2, 3, …​

Example:
* `list` followed by `archive 2` archives the 2nd patient in HealthSync.

### Listing all patients in archive : `listarchive`

Shows a list of all patients being archived.

Format: `listarchive`

### Unarchiving a patient : `unarchive`

Remove the specified patient from archive list and add them back to HealthSync.

Format: `unarchive INDEX`

* Unarchive the patient at the specified `INDEX` from archive list.
* The index refers to the index number shown in the displayed patient list.
* The index **must be a positive integer** 1, 2, 3, …​

Example:
* `listarchive` followed by `unarchive 2` adds the 2nd patient in archive list back to HealthSync.

### Deleting a patient : `delete`

Deletes the specified patient from HealthSync.

Format: `delete INDEX`

* Deletes the patient at the specified `INDEX`.
* The index refers to the index number shown in the displayed patient list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd patient in HealthSync.
* `find Betsy` followed by `delete 1` deletes the 1st patient in the results of the `find` command.

### Clearing all entries : `clear`

Clears all entries from HealthSync.

Format: `clear`

--------------------------------------------------------------------------------------------------------------------

## Tag Management

<box type="info" seamless>

### Adding a tag: `ta/ ti/ tc/`

* Adds a tag to a patient based on their patient_ID in the address book.
* Tags can be added for allergies (ta/), insurance (ti/), or conditions (tc/).
* Tags are case-sensitive and need to be alphanumeric.
* Tags can be more than 1 word in length.

Format: `tag <patient_ID> ta/ALLERGY`
         `tag <patient_ID> ti/INSURANCE`
         `tag <patient_ID> tc/CONDITION`

<box type="tip" seamless>

**Tip:** Add the tags based on their different categories such as allergy (`ta/`), insurance (`ti/`), and condition (`tc/`).
</box>

Examples:
* `tag 1 ta/peanuts` assigns an allergy tag 'peanuts' to the patient at index 1.
  <img src="AddTagExample.png" width="300" height="200">
* `tag 2 ti/prudential`
* `tag 3 tc/diabetes`

**⚠️ Warning:** If the tag already exists for the patient, it will not be added again.

---

### Deleting a tag: `td/`

* Deletes a tag from a patient based on their patient_ID in the address book.
* Tags are case-sensitive.

Format: `tag <patient_ID> td/TAGNAME`

<box type="tip" seamless>

**Tip:** You can undo the command if the tag was deleted by mistake.
</box>

Examples:
* `tag 1 td/peanuts`
  <img src="DeleteTagExample.png" width="300" height="200">
* `tag 2 td/diabetes`

--------------------------------------------------------------------------------------------------------------------

### Undoing a command: `undo`

Reverts the last command that modified data.

Format: `undo`

**⚠️ Warning:**
* Cannot be used repeatedly to undo multiple actions.
* Cannot undo `undo`, `redo`, `help`, or `exit` commands.

Example:
* `undo` (Restores the state before the last action)

### Redoing a command: `redo`

Restores the last undone command.

Format: `redo`

**⚠️ Warning:**
* Can only be used if `undo` was previously executed.
* Cannot redo commands that were not undone.

Example:
* `redo` (Restores the last undone action)

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

HealthSync data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

HealthSync data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>
**⚠️ Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimise the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimised, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| **Action**                 | **Format, Examples**                                                                                                             |
|----------------------------|----------------------------------------------------------------------------------------------------------------------------------|
| **Add Patient**            | `add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]…​`<br>e.g., `add n/John Doe p/98765432 e/john@example.com a/123 Street t/diabetes` |
| **Edit Patient**           | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br>e.g., `edit 2 n/John Smith p/91234567`                        |
| **Delete Patient**         | `delete INDEX`<br>e.g., `delete 3`                                                                                               |
| **Find Patient**           | `find KEYWORD [MORE_KEYWORDS]`<br>e.g., `find John`                                                                              |
| **List Patients**          | `list`                                                                                                                           |
| **Schedule Appointment**   | `schedule INDEX [DD-MM-YYYY HH:MM]`<br>e.g, `schedule 1 30-03-2026 12:00`                                                        |
| **Sort Patients**          | `sort FIELD`<br>e.g., `sort name`                                                                                                |
| **Set Emergency Contact**  | `emergency INDEX n/NAME p/PHONE r/RELATIONSHIP`<br>e.g., `emergency 1 n/Jane Doe p/81234567 r/Mother`                            |
| **Archive Patient**        | `archive INDEX`<br>e.g., `archive 2`                                                                                             |
| **List Archived Patients** | `listarchive`                                                                                                                    |
| **Unarchive Patient**      | `unarchive INDEX`<br>e.g., `unarchive 2`                                                                                         |
| **Clear All Entries**      | `clear`                                                                                                                          |
| **Undo Command**           | `undo`                                                                                                                           |
| **Redo Command**           | `redo`                                                                                                                           |
| **Add Allergy Tag**        | `tag INDEX ta/ALLERGY`<br>e.g., `tag 1 ta/peanuts`                                                                               |
| **Add Condition Tag**      | `tag INDEX tc/CONDITION`<br>e.g., `tag 1 tc/asthma`                                                                              |
| **Add Insurance Tag**      | `tag INDEX ti/INSURANCE`<br>e.g., `tag 1 ti/medishield`                                                                          |
| **Delete Tag**             | `tag INDEX td/TAGNAME`<br>e.g., `tag 1 td/peanuts`                                                                               |
| **Help**                   | `help`                                                                                                                           |
| **Exit**                   | `exit`                                                                                                                           |
