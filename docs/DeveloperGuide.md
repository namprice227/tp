---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# HealthSync Developer Guide

## Table of Contents

- [Acknowledgements](#acknowledgements)
- [Setting Up, Getting Started](#setting-up-getting-started)
- [Design](#design)
    - [Architecture](#architecture)
    - [UI Component](#ui-component)
    - [Logic Component](#logic-component)
    - [Model Component](#model-component)
    - [Storage Component](#storage-component)
    - [Common Classes](#common-classes)
- [Implementation](#implementation)
    - [Undo/Redo Feature](#undoredo-feature)
- [Documentation, Logging, Testing, Configuration, DevOps](#documentation-logging-testing-configuration-devops)
- [Appendix: Requirements](#appendix-requirements)
    - [Product Scope](#product-scope)
    - [User Stories](#user-stories)
    - [Use Cases](#use-cases)
    - [Non-Functional Requirements](#non-functional-requirements)
- [Appendix: Instructions for Manual Testing](#appendix-instructions-for-manual-testing)
- [Glossary](#glossary)
- [Appendix: Planned Enhancements](#appendix-planned-enhancements)
- [Appendix: Effort](#appendix-effort)
<div style="page-break-after: always;"></div>

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the AddressBook-Level3 project created by the SE-EDU initiative.
* The undo and redo features were inspired by the proposed implementation found in the [AddressBook-Level 3's Developer Guide](https://se-education.org/addressbook-level3/DeveloperGuide.html#proposed-undoredo-feature).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

<div style="page-break-after: always;"></div>

## **Design**
:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The **Architecture Diagram** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />


Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI Component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103T-F11-3/tp/blob/master/src/main/java/seedu/address/ui/Ui.java).

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

#### Overview

The `MainWindow` serves as the primary container for all UI components, orchestrating interactions between different elements and ensuring a seamless user experience for healthcare administrators managing patient records. Its layout is defined in [`MainWindow.fxml`](https://github.com/AY2425S2-CS2103T-F11-3/tp/blob/master/src/main/resources/view/MainWindow.fxml), which specifies the structure and arrangement of UI elements using JavaFX's XML-based markup.

The UI consists of a `MainWindow` that is composed of multiple UI parts, such as:

- **`CommandBox`** – Handles user input, enabling administrators to quickly search, add, or update patient data.
- **`ResultDisplay`** – Displays the results of executed commands, offering immediate feedback on operations (e.g., updating a patient's record).
- **`PersonListPanel`** – Manages the list of patients (each represented as a `Person`) displayed on the left side, facilitating easy browsing and selection of patient records.
- **`PersonCard`** – Represents a summarized view of a patient within the `PersonListPanel`, highlighting essential details such as the patient's name and contact information.
- **`PersonDetailPanel`** – Displays detailed attributes of the selected patient on the right side, ensuring that administrators have a comprehensive view of patient data.
- **`PersonDetail`** – Controls which patient details are shown in the `PersonDetailPanel`, ensuring that only relevant information is displayed.
- **`StatusBarFooter`** – Shows application status details, such as the number of patients currently loaded and other system notifications.

All UI components, including `MainWindow`, inherit from the abstract `UiPart` class, which encapsulates common UI functionalities and ensures consistency across the application.

#### Responsibilities of the UI Component
The `UI` component is responsible for managing the interface through which healthcare administrators interact with patient records. It:
- Executes user commands by interacting with the `Logic` component.
- Listens for changes in `Model` data (which includes patient records) and updates the UI accordingly.
- Maintains a reference to the `Logic` component, as the UI depends on it to process user actions.
- Displays `Person` objects (representing patient records) retrieved from the `Model` component.

#### Layout & Functionality

- **Main Window (`MainWindow.fxml`)**:  
  The central container that defines the overall structure of the application, ensuring a seamless user experience in managing patient data.
    - `MainWindow` initializes and manages key UI components such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, and `PersonDetailPanel`.
    - It listens to user inputs from healthcare administrators and updates the display accordingly.

- **Left Panel:**  
  The `PersonListPanel` displays a list of patients. Each patient's summary is represented by a `PersonCard`, allowing administrators to quickly scan through patient records.

- **Right Panel:**  
  The `PersonDetailPanel` presents detailed information about the selected patient, with `PersonDetail` managing which fields are displayed to provide a comprehensive view of the patient's information.



### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103T-F11-3/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103T-F11-3/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />

The **Model** component encapsulates and manages the core data of the application while remaining independent of the UI, Logic, and Storage components. Its responsibilities include:

- **Active Patient Data Management:**  
  Active patient data is stored in a `VersionedAddressBook`, which maintains a complete collection of `Person` objects in a `UniquePersonList`. This design supports undo/redo functionality by preserving previous states of the address book.

- **Archived Patient Data Management:**  
  Archived patient data is managed separately via an `ArchivedBook`, which implements both the `ArchiveBook` and `ReadOnlyArchivedBook` interfaces. This separation ensures that archived records remain distinct from active records while still providing controlled, read-only access.

- **Filtered Person List:**  
  The Model keeps a filtered list of `Person` objects (e.g., as a result of search queries) as an unmodifiable `ObservableList<Person>`. The UI binds to this list so that it automatically updates when the underlying data changes.

- **User Preferences:**  
  User-specific preferences are stored in a `UserPrefs` object, which is exposed externally as a `ReadOnlyUserPrefs` to prevent unwanted modifications by other components.


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103T-F11-3/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,

* can save patient record data, archived patient data, and user preference data in JSON format, and read them back into corresponding objects.  
* inherits from `AddressBookStorage`, `ArchivedBookStorage`, and `UserPrefsStorage`, which means it can be treated as any of them (depending on the required functionality in a given context).  
* delegates the actual reading and writing of JSON data to specific implementations like `JsonAddressBookStorage`, `JsonArchivedBookStorage`, and `JsonUserPrefsStorage`.
* uses helper classes such as `JsonSerializableAddressBook` and `JsonSerializableArchivedBook` to convert between model data and JSON.  
* further relies on classes like `JsonAdaptedPerson` and `JsonAdaptedTag` to handle conversion of individual elements within the model.  
* depends on some classes in the Model component (because the Storage component’s job is to save/retrieve objects that belong to the Model).

**Note:** Although the storage file is named `addressbook.json`, it actually stores patient data.
**Note:** Although the storage file is named `archivedbook.json`, it actually stores archived patient data.


### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Undo/redo feature

#### Implementation

The undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history, maintains a limited history of two states, and removes older states.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command sets `isLastCommandArchiveRelated` to `false` and calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`. At this point, `addressBookStateList` will contain only the two most recent states due to the limit implemented in `commit()`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. Before executing the undo operation, the `UndoCommand` checks if the last command was archive-related by calling `model.isLastCommandArchiveRelated()`. If it was, the undo operation is not allowed, and an error message is shown. Otherwise, the `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. The implementation also ensures that only the two most recent states are kept in memory by removing older states when a new state is added. This approach helps manage memory usage while still allowing for basic undo/redo functionality.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book with a limited history of two states.
  * Pros: Easy to implement and memory efficient.
  * Cons: Limited to only undoing the most recent operation.

* **Alternative 2:** Individual command knows how to undo/redo by itself with unlimited history.
  * Pros: Will use less memory per state (e.g. for `delete`, just save the person being deleted).
  * Cons: More complex implementation required for each command.
  
**Aspect: Handling of archive-related commands:**

* **Alternative 1 (current choice):** Prevent undo operations for archive-related commands.
  * Pros: Simplifies the implementation and prevents unexpected behavior with archived data.
  * Cons: Reduces flexibility for users who might want to undo archiving operations.

* **Alternative 2:** Support undo for all command types including archive operations.
  * Pros: Provides a consistent user experience across all commands.
  * Cons: Requires more complex state management to handle transitions between normal and archive modes.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:
* User Role: Healthcare administrators
* Workplace: Family clinics
* Responsibilities
  * Maintain and update patient records and associated emergency contact information.
  * Effortlessly monitor and stay on top of patients’ upcoming appointments, reducing scheduling conflicts and ensuring prompt follow-ups.
  * Efficiently manage and update essential patient data, including insurance details, allergies, medical conditions, and treatment history, to ensure comprehensive and accurate medical records.

**Value proposition**

HealthSync allows healthcare staff to efficiently organize patient records and manage critical emergency contact details within one unified platform
By providing quick access to up-to-date information, administrators can seamlessly connect with patients' emergency contacts, ensuring efficient communication and prompt action, particularly in managing recovery progress and treatment schedules.

### User Stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                                                   | So that I can…​                                                                                      |
|----------|--------------------------------------------|----------------------------------------------------------------|------------------------------------------------------------------------------------------------------|
| `* * *`  | healthcare administrator                   | quickly locate a patient's emergency contacts                  | promptly notify their next of kin in life-threatening medical situations                             |
| `* * *`  | healthcare administrator                   | accurately upload and store hospital patients' contact details | ensure their information is reliably available for communication and record-keeping                  |
| `* * *`  | healthcare professional                    | quickly identify a patient's medical conditions and allergies  | understand the patient’s condition and provide safe and effective care                               |
| `* * *`  | new user                                   | have sample data to work with                                  | understand how to use the application                                                                |
| `* * *`  | healthcare administrator                   | schedule an appointment for a patient                          | keep track of upcoming visits to the clinic                                                          |
| `* * *`  | healthcare administrator                   | efficiently update and manage patient schedules                | ensure that doctors' time is managed well and appointments do not overlap                            |
| `* * *`  | healthcare administrator                   | identify patient allergies using tags                          | prescribe medicines safely and avoid malpractice                                                     |
| `* * *`  | healthcare administrator                   | have an overview of all current patients                       | quickly access and manage active patient records                                                     |
| `* * *`  | healthcare administrator                   | add an insurance tag to a patient                              | document their insurance details for billing and verification purposes                               |
| `* * *`  | healthcare administrator                   | add a medical condition tag to a patient                       | keep a clear medical history for safe treatment and reference                                        |
| `* * *`  | healthcare administrator                   | add a new patient to the system                                | begin tracking their appointments, contact, and medical details                                      |
| `* * *`  | healthcare administrator                   | edit a patient's details                                       | keep their records accurate and up to date                                                           |
| `* * *`  | healthcare administrator                   | find a patient by name                                         | quickly bring up a specific patient’s record                                                         |
| `* * *`  | user                                       | access help at any time                                        | see available commands and how to use them                                                           |
| `* *`    | healthcare administrator                   | undo the last operation                                        | recover from accidental changes or deletions                                                         |
| `* *`    | healthcare administrator                   | redo the last undone operation                                 | reverse an undo if it was done by mistake                                                            |
| `* *`    | healthcare administrator                   | delete a specific tag from a patient                           | remove outdated or incorrect medical information                                                     |
| `*`      | user with many patients                    | sort patients by name                                          | locate a patient quickly and efficiently                                                             |
| `*`      | healthcare administrator                   | archive a patient who has not visited in years                 | keep the list of current patients clean and manageable                                               |
| `*`      | healthcare administrator                   | unarchive a patient from the archive list                      | update their information if they return to the clinic                                                |
| `*`      | healthcare administrator                   | view a list of all archived patients                           | easily access records of patients who are no longer active                                           |
| `*`      | healthcare administrator                   | clear all patient entries                                      | reset the system when starting fresh or during testing phases                                        |
| `*`      | healthcare administrator                   | delete a patient from the system                               | remove incorrect or duplicated records                                                               |
| `*`      | user                                       | exit the application                                           | close the programme when I’m done using it                                                           |

### Use Cases

(For all use cases below, the **System** is `HealthSync` and the **Actor** is the `Healthcare Administrator`, unless specified otherwise.)

---

#### **UC01 - View Help Window**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to view the help window.
2. HealthSync displays the link to the user guide.
3. Use case ends.

---

#### **UC02 - Add Patient**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to add a new patient.
2. HealthSync prompts the user to enter:
    - Patient's name.
    - Patient's phone number.
    - Patient's email address.
    - Patient's residential address.
3. Healthcare Administrator enters the requested details.
4. HealthSync successfully adds the patient.
5. Use case ends.

**Extensions:**
- **3a.** Healthcare Administrator enters incomplete or invalid details (e.g. phone number contains letters).
    - **3a1.** HealthSync informs the user of the errors.
    - Use case resumes from step **2**.
- **3b.** A patient with the same unique identifier (i.e. name and phone number, name and email address) already exists.
    - **3b1.** HealthSync notifies the user of the duplication.
    - **3b2.** Healthcare Administrator can choose to:
        -  **3b2a.** Update the existing record:
          - **3b2a1.** Transition to **UC02 - Edit Patient Details**.
        -  **3b2b.** Cancel the addition:
          - **3b2b1.** Use case ends.
        -  **3b2c.** Provide corrected details:
          - **3b2c1.** Use case resumes from step **2**.
- **4a.** Healthcare Administrator chooses to undo the command.
    - **4a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC03 - Update Patient Schedule**

**Main Success Scenario (MSS):**
1. Healthcare Administrator requests to update a patient’s appointment.
2. HealthSync prompts the user to enter:
    - Patient index.
    - New or modified appointment details (i.e. appointment date, time).
3. Healthcare Administrator inputs the updated appointment.
4. HealthSync updates the patient’s appointment successfully.
5. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range or does not exist).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **3b.** The entered appointment details are invalid (e.g., overlapping appointments, incorrect format).
    - **3b1.** HealthSync displays an error message.
    - **3b2.** Healthcare Administrator corrects the details and resubmits.
    - Use case resumes from step **2**.
- **4a.** Healthcare Administrator chooses to undo the command.
    - **4a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC04 - List Patients**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to list all patients.
2. HealthSync displays the complete list of patients.
3. Use case ends.

---

#### **UC05 - Sort Patients by Name**

**Main Success Scenario (MSS):**
1. Healthcare Administrator requests to sort the list of patients by name.
2. HealthSync sorts the patient list in ascending alphabetically order.
3. HealthSync displays the sorted patient list.
4. Use case ends.

**Extensions:**
- **2a.** Healthcare Administrator chooses to undo the command.
    - **2a1.** Transition to **UC09 - Undo Previous Command**.

---

### **UC06 - Sort Patients by Appointment**

**Main Success Scenario (MSS)**
1. Healthcare Administrator requests to sort the list of patients by appointment.
2. HealthSync sorts the patient list in ascending lexicographical order by appointment date.
3. HealthSync displays the sorted patient list with patients having appointments listed first (nearest upcoming appointments at the top), followed by patients without appointments.
4. Use case ends.

**Extensions:**

- **3a.** Healthcare Administrator chooses to undo the command.
    - **3a1.** Transition to **UC09 - Undo Previous Command.**
- **3b.** The sorting causes unexpected results due to invalid appointment data.

    - **3b1.** HealthSync displays an error message.
    - **3b2**. HealthSync reverts to the original order.
    - Use case resumes from step 1.

#### **UC07 - Edit Patient Details**

**Main Success Scenario (MSS):**
1. Healthcare Administrator requests to edit a patient’s details.
2. HealthSync prompts the user to enter the following details:
    - Index of the patient to be edited.
    - New information for the field to be updated.
3. Healthcare Administrator inputs the new information.
4. HealthSync asks for confirmation.
5. Healthcare Administrator confirms the edit.
6. HealthSync updates the patient’s details successfully.
7. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **3b.** The entered details are invalid (e.g. phone number contains letters).
    - **3b1.** HealthSync informs the user of the invalid details.
    - Use case resumes from step **2**.
- **4a.** Healthcare Administrator does not confirm the action.
    - **4a1.** HealthSync cancels the operation.
    - Use case ends.
- **4b.** Healthcare Administrator inputs an invalid response (not 'y' or 'n') when confirming.
    - **4b1.** HealthSync displays an error message indicating the invalid input.
    - **4b2.** Action is cancelled.
    - Use case ends.
- **6a.** Healthcare Administrator chooses to undo the command.
    - **6a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC08 - Add an Emergency Contact**

**Main Success Scenario (MSS):**
1. Healthcare Administrator requests to add an emergency contact for a patient.
2. HealthSync prompts the user to enter:
    - Patient index.
    - Emergency contact’s name.
    - Emergency contact’s phone number.
    - Relationship to the patient.
3. Healthcare Administrator provides the details.
4. HealthSync adds the emergency contact to the patient’s record.
5. HealthSync confirms successful addition.
6. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range or does not exist).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **3b.** The entered details are invalid (e.g. phone number contains letters).
    - **3b1.** HealthSync informs the user of the invalid details.
    - Use case resumes from step **2**.
- **5a.** Healthcare Administrator chooses to undo the command.
    - **5a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC09 - Find Patients**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to find patients by name.
2. HealthSync prompts the Healthcare Administrator to enter keywords for the search.
3. Healthcare Administrator enters the keywords (case-insensitive).
4. HealthSync searches the patients' name and phone number for the given keywords.
5. HealthSync displays the list of patients whose details match at least one of the entered keywords.
6. Use case ends.

**Extensions:**
- **4a.** No patients match the given keywords.
    - **4a1.** HealthSync displays a message informing the user that 0 matches were found.
    - Use case ends.

---

#### **UC10 - Archive Patient**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to archive a patient.
2. HealthSync prompts the Healthcare Administrator to enter the index of the patient to be archived.
3. Healthcare Administrator provides the patient index.
4. HealthSync archives the patient’s record.
5. HealthSync confirms successful archiving.
6. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range or does not exist).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **5a.** Healthcare Administrator chooses to undo the command.
    - **5a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC11 - List Archived Patients**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to view the list of archived patients.
2. HealthSync retrieves and displays the list of archived patients.
3. HealthSync confirms successful display of list of archived patients.
4. Use case ends.

**Extensions:**
- **2a.** Healthcare Administrator tries to use any command other than `find`, `unarchive`, or `list` in archive mode.
    - **2a1.** HealthSync informs the user that the command cannot be executed in archive mode.
    - **2a2.** Use case ends.

---

#### **UC12 - Unarchive Patient**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to unarchive a patient.
2. HealthSync prompts the user to enter the index of the patient to be unarchived.
3. Healthcare Administrator provides the patient index.
4. HealthSync unarchives the patient’s record.
5. HealthSync confirms successful unarchiving.
6. Use case ends.

**Extensions:**
- **2a.** The given index is invalid (out of range or does not exist).
    - **2a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.

---

#### **UC13 - Delete a Patient**

**Main Success Scenario (MSS):**
1. Healthcare Administrator requests to delete a patient’s contact.
2. HealthSync prompts the user to enter the index of the patient to be deleted.
3. Healthcare Administrator provides the patient index.
4. HealthSync asks for confirmation.
5. Healthcare Administrator confirms the deletion.
6. HealthSync deletes the patient’s contact from the system.
7. HealthSync confirms successful deletion.
8. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range or does not exist).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **4a.** Healthcare Administrator does not confirm the action.
    - **4a1.** HealthSync cancels the operation.
    - Use case ends.
- **4b.** Healthcare Administrator inputs an invalid response (not 'y' or 'n') when confirming.
    - **4b1.** HealthSync displays an error message indicating the invalid input.
    - **4b2.** Action is cancelled.
    - Use case ends.
- **7a.** Healthcare Administrator chooses to undo the command.
    - **7a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC14 - Clear Patient List**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to clear the entire patient list.
2. HealthSync asks for confirmation.
3. Healthcare Administrator confirms the edit.
4. HealthSync clears all patients from the system.
5. HealthSync confirms the list has been cleared.
6. Use case ends.

**Extensions:**
- **3a.** Healthcare Administrator does not confirm the action.
    - **3a1.** HealthSync cancels the operation.
    - Use case ends.
- **3b.** Healthcare Administrator inputs an invalid response (not 'y' or 'n') when confirming.
    - **3b1.** HealthSync displays an error message indicating the invalid input.
    - **3b3.** Action is cancelled.
    - Use case ends.
- **5a.** Healthcare Administrator chooses to undo the command.
    - **5a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC15 - Add Tag to Patient**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to add tag(s) to a patient.
2. HealthSync prompts the user to enter:
    - Patient index.
    - Tag(s) to add.
3. Healthcare Administrator enters the index and the tag(s).
4. HealthSync validates the index and tags.
5. HealthSync adds the valid tags to the patient and displays the updated tag list.
6. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range or does not exist).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **3b.** The tag(s) input is invalid (e.g. tag name is not alphanumeric).
    - **3b1.** HealthSync informs the user of the invalid input.
    - Use case resumes from step **2**.
- **4a.** The tag(s) already exist.
    - **4a1.** HealthSync informs the user of the duplication.
    - Use case resumes from step **2**.
- **5a.** Healthcare Administrator chooses to undo the command.
    - **5a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC16 - Delete Tag from Patient**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to delete tag(s) from a patient.
2. HealthSync prompts the user to enter:
    - Patient index.
    - Tag(s) to delete.
3. Healthcare Administrator enters the index and tag(s).
4. HealthSync checks if the tag(s) exist.
5. HealthSync removes valid tag(s) and shows the updated tag list.
6. Use case ends.

**Extensions:**
- **3a.** The given index is invalid (out of range or does not exist).
    - **3a1.** HealthSync informs the user of the invalid index.
    - Use case resumes from step **2**.
- **4a.** The tag(s) do not exist.
    - **4a1.** HealthSync warns the user of the invalid tag.
    - Use case resumes from step **2**.
- **5a.** Healthcare Administrator chooses to undo the command.
    - **5a1.** Transition to **UC09 - Undo Previous Command**.

---

#### **UC17 - Undo Previous Command**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to undo the most recent modifying command.
2. HealthSync checks if there is a previous state to revert to.
3. HealthSync reverts the application to the previous state.
4. HealthSync confirms the undo action.
5. Use case ends.

**Extensions:**
- **2a.** No previous state available to undo.
    - **2a1.** HealthSync informs the user that there is nothing to undo.
    - Use case ends.
- **2b.** Undo has already been used.
    - **2b1.** HealthSync informs the user that there is nothing to undo.
    - Use case ends.
- **2c.** The most recent command is a `undo`, `redo` or `find` or `list` command.
    - **2c1.** HealthSync informs the user that there is nothing to undo.
    - Use case ends.
- **2d.** The most recent command is an `archive` command.
    - **2d1.** HealthSync informs the user that undo cannot be performed for the archive command.
    - Use case ends.

---

#### **UC18 - Redo Last Undone Command**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to redo the last undone command.
2. HealthSync checks if there is a redo-able state.
3. HealthSync re-applies the previously undone command.
4. HealthSync confirms the redo action.
5. Use case ends.

**Extensions:**
- **2a.** No command available to redo.
    - **2a1.** HealthSync informs the user that there is nothing to redo.
    - Use case ends.

---

#### **UC19 - Exit Application**

**Main Success Scenario (MSS):**
1. Healthcare Administrator chooses to exit the application.
2. HealthSync saves the application state.
3. HealthSync closes the application.
4. Use case ends.

---

### Non-Functional Requirements
1. Reliability and Availability:
   * System Uptime:
      HealthSync must be available for use at least 99% of the time, especially during clinic operating hours. Regular maintenance should be scheduled during off-peak times.

   * Memory Recovery and Backup:
      Patient data must be backed up daily to prevent data loss. The system should be able to recover from a backup within 2 hours of a failure.


2. Performance Requirements:
   *  Responsiveness: HealthSync should respond to user commands (e.g., adding, editing, or viewing patient data) within 3 seconds for typical operations under normal usage conditions (i.e., up to 1000 patient records in the database).

   * Scalability:
   HealthSync should be able to handle up to 5000 patient records without significant performance degradation. Basic operations, such as editing or adding patient records, should not exceed a response time of 4 seconds under this load.

   * Concurrency: HealthSync should be able to handle up to 50 concurrent patients without noticeable sluggishness in performance for typical use.


3. Usability Requirements:
   * Command Efficiency:
   A user with above-average typing speed for regular English text (i.e., not code or system admin commands) should be able to accomplish most tasks faster using commands than with the mouse.

    * Error Handling and Feedback:
The system must provide immediate feedback (within 1 second) when an error occurs, such as invalid input or missing fields. The user should be able to correct errors without restarting the operation.


4. Data and Storage Requirements:
   * Human-Editable File Format:
   Patient information should be stored in a human-readable and editable format (e.g., .json or .csv) so that administrators can manually access and modify data if needed.

    * Data Integrity: The system must ensure that no data is lost or corrupted during common operations (e.g., adding, updating, or deleting records). Transaction-like behavior must be implemented to ensure all data operations either succeed fully or fail without partially corrupting data.


5. Compliance and Security:
   * Healthcare Data Protection Compliance:
   HealthSync must comply with relevant healthcare data protection regulations, such as PDPA, to ensure that patient data is handled securely and confidentially.


6. Compatibility and Portability:
   * Cross-Platform Support:
   HealthSync must be compatible with mainstream operating systems (Windows, macOS, Linux) and function seamlessly on systems with Java 17 or higher installed.


7. Maintainability and Extensibility:
   * Modular Design:
   The system should be designed with a modular architecture, allowing for easy future extensions such as additional data fields, user roles, or features (e.g., appointment scheduling or integration with electronic health records).

   * Testability:
HealthSync must be easily testable, with automated tests that can cover at least 70% of the codebase. Core features (e.g., adding a patient record, deleting outdated patient records) should have dedicated test cases.
--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for Manual Testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial Launch

   1. Download the latest jar file [here](https://github.com/AY2425S2-CS2103T-F11-3/tp/releases) and copy into an empty folder

   2. Open a terminal window and use the `cd` command into the same folder
   
   3. Enter `java -jar HealthSync.jar` into the terminal. The window size may not be optimum initially but it can be sized.

2. Saving Window Preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. Shutdown
    There are multiple ways to exit the application:

   1. Use the `exit` command.
   
   2. Click on the red cross on the application window.

   3. Click `File` in the top left corner, then `Exit` in the dropdown menu.

<div style="page-break-after: always;"></div>

### Testable Features

#### 1. Scheduling an Appointment

1. **Scheduling an appointment successfully**
    - **Prerequisites:** A patient is already added and visible in the list. No upcoming appointment exists for the patient.
    - **Test case:** `schedule 1 12-04-2025 14:30`  
      **Expected:** Appointment is scheduled for patient at index 1. A confirmation message is shown, including the updated appointment details. The scheduled date and time are in the future, and there is a minimum gap of 15 minutes from any other appointment.

2. **Scheduling an appointment in the past**
    - **Prerequisites:** A patient exists.
    - **Test case:** `schedule 1 01-01-2020 10:00`  
      **Expected:** No appointment is scheduled. An error message is displayed indicating that the appointment must be set in the future.

3. **Scheduling a duplicate appointment**
    - **Prerequisites:** A patient already has an appointment scheduled at a specific date and time.
    - **Test case:** `schedule 1 12-04-2025 14:30` (if already scheduled)  
      **Expected:** No new appointment is created. An error message is displayed stating that duplicate appointments are not permitted.

4. **Scheduling with insufficient gap between appointments**
    - **Prerequisites:** The patient already has an appointment scheduled by another person (or for a different reason) at a specific time.
    - **Test case:** `schedule 1 12-04-2025 14:40` (if there is another appointment within 15 minutes of the new appointment)
      **Expected:** The system displays an error message indicating that the appointment cannot be scheduled because another appointment is within 15 minutes.

---

#### 2. Sort

1. **Sort patients by name**
    - **Prerequisites:** Multiple patients exist in the list, with varying names.
    - **Test case:** `sort name`  
      **Expected:** The patient list is sorted in ascending alphabetical order by name. In case of duplicate names, the most recently added patient appears first.

2. **Sort patients by appointment**
    - **Prerequisites:** Some patients have appointments while others do not.
    - **Test case:** `sort appointment`  
      **Expected:** Patients with appointments are listed first, sorted in lexicographical order by appointment date (nearest upcoming appointment at the top), followed by patients without appointments.

3. **Invalid sort field**
    - **Test case:** `sort invalidField`
      **Expected:** The system displays an error message stating that the specified field is not available for sorting.

---

#### 3. Setting Emergency Contact

1. **Setting a valid emergency contact**
    - **Prerequisites:** A patient is present in the list.
    - **Test case:** `emergency 1 n/Alden Tan p/98765432 r/Boyfriend`
      **Expected:** Emergency contact details for the patient at index 1 are updated. A confirmation message is displayed showing the new emergency contact details.

2. **Incomplete emergency contact details**
    - **Test case:** `emergency 1 n/Alden Tan p/98765432` (missing relationship)  
      **Expected:** The system displays an error message indicating that all fields (name, phone, relationship) are required.

3. **Invalid patient index for emergency contact**
    - **Test case:** `emergency 0 n/Alden Tan p/98765432 r/Boyfriend`  
      **Expected:** No update is made. The system shows an error message regarding the invalid patient index.

---

#### 4. Archiving a Patient

1. **Archiving a patient successfully**
    - **Prerequisites:** The patient list is visible with multiple patients (use the `list` command first).
    - **Test case:** `archive 2`  
      **Expected:** The patient at index 2 is moved from the main patient list to the archive list. A confirmation message is displayed.

2. **Archiving with an invalid index**
    - **Test case:** `archive 0` or `archive 100` (when index is out of range)  
      **Expected:** No patient is archived. An error message is shown regarding the invalid index.

3. **Attempting to archive while in archive mode**
    - **Prerequisites:** The system is in archive mode (viewing archived patients).
    - **Test case:** `archive 1`
      **Expected:** The system informs the user that the archive command is not available while viewing the archived patient list.

---

#### 5. Listing All Patients in Archive

1. **Listing archived patients when archive is non-empty**
    - **Prerequisites:** At least one patient has been archived (use the `archive` command first).
    - **Test case:** `listarchive`
      **Expected:** The system retrieves and displays the list of archived patients along with a confirmation message.

2. **Listing archived patients when there are no archived patients**
    - **Prerequisites:** No patients have been archived.
    - **Test case:** `listarchive` (when archive is empty)  
      **Expected:** The system retrieves and displays an empty list of archived patients along with a confirmation message.

3. **Attempting restricted commands in archive mode**
    - **Prerequisites:** The system is in archive mode (use the `listarchive` command first).
    - **Test case:** Use any command other than `find` or `unarchive` (e.g., `delete 1`)  
      **Expected:** The system displays a message indicating that the command cannot be executed in archive mode.

---

#### 6. Unarchiving a Patient

1. **Unarchiving a patient successfully**
    - **Prerequisites:** The archive list contains at least one patient (use the `archive` command first).
    - **Test case:** `unarchive 1` 
      **Expected:** The patient at index 1 in the archive list is moved back to the main patient list. A confirmation message is displayed.

2. **Unarchiving with an invalid index**
    - **Test case:** `unarchive 0` or `unarchive 10` (if the index is out of range)  
      **Expected:** No patient is unarchived. An error message is displayed regarding the invalid index.

3. **Attempting to use `unarchive` while not in archive mode**
    - **Prerequisites:** The system is in the main patient list view. (use the `list` command first)
    - **Test case:** `unarchive 1` 
      **Expected:** The system informs the user that the unarchive command is not available while viewing the main patient list.

---

#### 7. Adding a Tag

1. **Adding a new tag successfully**
    - **Prerequisites:** A patient exists in the list.
    - **Test case:** `tag 1 ta/peanuts` 
      **Expected:** The tag "peanuts" is added to the patient at index 1. A confirmation message is displayed showing the updated tag list.

2. **Attempting to add a duplicate tag**
    - **Prerequisites:** The patient already has the tag "asthma".
    - **Test case:** `tag 1 tc/asthma`  
      **Expected:** The system recognises the duplicate and does not add it. A message is displayed indicating that the tag already exists.

3. **Adding a tag with invalid format**
    - **Test case:** `tag 1 ti/!@#`
      **Expected:** No tag is added. The system displays an error message indicating that the tag must be alphanumeric.

---

#### 8. Undoing a Command

1. **Successful undo of a modifying command**
    - **Prerequisites:** A modifying command (e.g., `edit`, `delete`, `tag`) has been executed.
    - **Test case:** `undo`
      **Expected:** The system reverts to the state before the last modifying command and displays a confirmation message with the updated data.

2. **Attempting undo when no command is available to undo**
    - **Prerequisites:** No modifying command (e.g., `edit`, `delete`, `tag`) has been executed.
    - **Test case:** `undo` (when no prior modifying command exists)  
      **Expected:** No changes are made. An error message is displayed stating that there is nothing to undo.

3. **Attempting to perform undo twice in succession**
    - **Prerequisites:** A modifying command was undone (use the `undo` command first).
    - **Test case:** `undo` (executed again immediately)  
      **Expected:** The system displays an error message indicating that undo can only be done once and no further undo is possible.

4. **Attempting to undo an archive command**
    - **Prerequisites:** The most recent command is an `archive` command (use the `archive` command first).
    - **Test case:** `undo`  
      **Expected:** The system displays an error message stating that the archive command cannot be undone.

---

#### 9. Redoing a Command

1. **Successful redo after an undo**
    - **Prerequisites:** A modifying command was undone using `undo` (use the `undo` command first).
    - **Test case:** `redo` 
      **Expected:** The previously undone command is reapplied, and the system confirms the change by displaying the updated data.

2. **Attempting redo when no command is available to redo**
    - **Prerequisites:** The most recent command is not an `undo` command.
     - **Test case:** `redo` (when no prior undo has been executed)  
      **Expected:** No changes occur. An error message is shown stating that there is nothing to redo.

3. **Attempting to perform redo multiple times in succession**
    - **Prerequisites:** The most recent command is a `redo` command (use the `redo` command first).
     - **Test case:** `redo` (executed again immediately)  
      **Expected:** The system displays an error message indicating that redo cannot be performed repeatedly.


### Saving Data

1. **Data Persistence after Operations**
    - **Steps:**
        1. Perform various add, edit, or delete operations in HealthSync.
        2. Close the application.
        3. Re-launch the application.
    - **Expected:** Data should save automatically and persist after closing and reopening the app.

2. **Handling a Missing or Corrupted Data File**
    - **Issue:** If the data file located at `[JAR file location]/data/addressbook.json` is missing or becomes corrupted (e.g., due to manual editing errors), HealthSync will initialise with default (empty) data.
    - **Resolution Instructions:**
        1. **Verify the Data File:**
            - Check the `/data` folder to ensure that `addressbook.json` exists and is in the correct JSON format.
        2. **If the File is Missing or Corrupted:**
            - **Restore from Backup:** If you have a backup copy of the file, replace the corrupted or missing file with the backup.
            - **Proceed with Default Data:** If no backup is available, note that HealthSync will start with an empty data file, and an error or warning message will be displayed.
        3. **Preventive Measure:**
            - Always take a backup of the `addressbook.json` file before making manual edits, as improper changes can lead to data loss or unexpected application behaviour.
    - **Expected:** Upon re-launching the app:
        - HealthSync displays a warning or error message about the missing or corrupted data file.
        - The app initialises with an empty data file, allowing you to continue using the application.

--------------------------------------------------------------------------------------------------------------------

## **Glossary**
1. **Command-Line Interface (CLI):** A method of interacting with HealthSync through typed text commands.
2. **Emergency Contact:** The person to be notified in the event of a patient's emergency, associated with patient record. Their name, phone number, and relationship to the patient are all stored in HealthSync.
3. **Healthcare Administrator:** The primary user of HealthSync, responsible for managing patient and emergency contact details within a clinic environment.
4. **HealthSync:** A healthcare application designed to assist healthcare administrators in efficiently managing patient contact information within a clinic.
5. **Mainstream OS**: Windows, Linux, Unix, MacOS.
6. **Private patient detail**: A contact detail intended to remain confidential and not shared with others

--------------------------------------------------------------------------------------------------------------------
## **Appendix: Planned Enhancements**

The current version of HealthSync has some limitations, and we have outlined our plans for future improvements to enhance upcoming versions of HealthSync.

### Multiple Language Support
Currently, HealthSync is only available for usage in English. We recognise that our target users may not be able to read English proficiently or may only have non English names. Our planned enhancement is to translate HealthSync into other languages, such as Chinese and Tamil.

### Removal of past appointment dates
At present, HealthSync does not automatically remove or archive past appointment dates, which can clutter the system and make it harder to manage current and future appointments. We plan to implement an automatic system that will remove or archive past appointment dates, ensuring a cleaner, more efficient user experience by keeping only relevant and upcoming appointments visible.

### UI Update on Undo, Redo
Currently, when an undo and redo operation are performed, the UI does not update immediately to reflect the change. In a future release, we plan to enhance the the functionality so that the UI is updated in real time, providing healthcare administrators with immediate visual feedback and a more intuitive interaction experience.

### Enhanced Find Command Functionality
The current implementation of the find command does not strictly limit results by the complete name. For example, searching for "John Toh" displays all patients with names that include "John Toh" first, followed by those with partial matches like "John" or "TOh." Our planned enhancement aims to refine this functionality by prioritizing exact matches while still accommodating partial matches. This improvement will yield more precise search results, helping administrators locate patient records more efficiently.


--------------------------------------------------------------------------------------------------------------------
## **Appendix: Effort**
As Year 2 Computer Science students with limited software engineering project experience, building upon AddressBook3 (AB3) was particularly challenging for several reasons:

1) Our limited experience with GitHub and workflow management, including reviewing pull requests and resolving conflicts.
2) HealthSync, as a Brownfield project based on AB3, presented several challenges that required substantial effort from our team to overcome. One key challenge was efficiently managing multiple entity types, such as those in normal and archive modes, compared to AB3, which dealt with just a single entity type.
3) The time constraint, with the need to build a relatively bug-free product in just over six weeks.

However, as a group of five, we were able to collaborate effectively, dividing tasks efficiently to tackle challenges and build HealthSync.
