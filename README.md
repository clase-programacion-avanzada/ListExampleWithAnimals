# Animal Management Program

The Animal Management Program is a console-based application for managing animals and their vaccines. 
This program allows you to perform various operations, including adding animals, adding vaccines to animals, loading data from CSV files, and generating reports.
The purpose of this program is to demonstrate the use of Java collections, specifically the `List` interface and its implementations.
Also, it shows how to use Enums. 
## Prerequisites

Before running the program, make sure you have the following prerequisites installed:

- Java Development Kit (JDK) 17 - [Download JDK](https://adoptium.net/temurin/releases/)
- Git (optional) - [Download Git](https://git-scm.com/downloads)

## Getting Started

Follow these steps to get started with the Animal Management Program:

1. Clone the repository (if you have Git installed) or download the ZIP file and extract it to a directory of your choice.

   ```bash
   git@github.com:clase-programacion-avanzada/ListExampleWithAnimals.git
   ```
2. Open a terminal or command prompt and navigate to the project directory:

   ```bash
   cd /path/to/list-example-with-animals
   ```
3. Compile the program:

   ```bash
   ./gradlew build
   ```
4. Run the program:

   ```bash
   gradlew -q --console plain run
   ```


## Program Features

The program provides the following features through a menu-driven interface:

1. **Add Animal:** Allows you to add a new animal with a name and age.

2. **Add Vaccine to Animal:** Lets you add a vaccine to an existing animal by specifying the name, volume, and brand of the vaccine.

3. **Load Animals and Vaccines from CSV:** Load animal data from a CSV file. Optionally, you can load vaccine data as well.

4. **Print Report of Unique Brands:** Generate a report of unique vaccine brands used in the animals.

5. **Print Report of Animal Vaccines:** Generate a report of animals and the number of vaccines they have received.

6. **Print Report of Animals Pending on Next Application:** Generate a report of animals pending their next vaccine application.

7. **Exit:** Exit the program.

## Usage Instructions

- Follow the on-screen prompts and instructions to interact with the program.
- When loading data from CSV files, you can use the default file paths and delimiters or specify custom paths and delimiters.
- Make sure to input valid data and follow the program's instructions to ensure smooth operation.

## Contributing

If you'd like to contribute to the program's development, please fork the repository, make your changes, and submit a pull request.

## License

This program is open-source and available under the [MIT License](LICENSE).

