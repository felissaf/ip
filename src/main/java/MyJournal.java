import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * A class to create chatBot called MyJournal.
 *
 * @author Felissa Faustine
 */
public class MyJournal {
    /**
     * Prints out the statement after a task is added.
     *
     * @param taskList The list of all tasks.
     * @return The statement to be printed after a task is added.
     */
    public static String taskAddPrint(ArrayList<Task> taskList) {
        return "Okay!! I've added the following task:\n"
                + taskList.get(taskList.size() - 1) + "\n"
                + "Now you have " + taskList.size() + " in the list";
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getTimeDate(Scanner line) {
        String parsed = "";
        while (line.hasNext()) {
            String currWord = line.next();
            if (isDate(currWord)) {
                LocalDate date = LocalDate.parse(currWord);
                String month = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                String dateFormatted = date.getDayOfWeek().toString() + ", " + date.getDayOfMonth() + " " + month
                        + " " + date.getYear();
                parsed = parsed + " " + dateFormatted;
            } else if (isTime(currWord)) {
                LocalTime time = LocalTime.parse(currWord);
                String beforeOrAfterNoon = time.getHour() >= 12 ? "pm" : "am";
                int hour = time.getHour() >= 12 ? time.getHour() - 12 : time.getHour();
                int min = time.getMinute();
                String timeFormatted = (String.valueOf(hour).length() == 1 ? "0" + hour : hour)
                        + ":" + (min < 10 ? "0" + min : min) + beforeOrAfterNoon;
                parsed = parsed + " " + timeFormatted;
            } else {
                parsed = parsed + " " + currWord;
            }
        }
        return parsed;
    }

    public static boolean isDate(String string) {
        return string.length() == 10 && string.charAt(4) == '-' && string.charAt(7) == '-'
                && isInteger(string.substring(0, 4)) && isInteger(string.substring(5, 7))
                && isInteger(string.substring(8, 10));
    }

    public static boolean isTime(String string) {
        return string.length() == 5 && isInteger(string.substring(0,2))
                && isInteger(string.substring(3,5)) && string.charAt(2) == ':';
    }

    /**
     * The main method of the MyJournal class.
     *
     * @param args An input of an array of strings.
     */
    public static void main(String[] args) {
        ArrayList<Task> items = new ArrayList<>();
        String input;
        Scanner reader = new Scanner(System.in);
        System.out.println("Hello!\n"
                + "1. Type a task (todo/event/deadline) to be added into your task list.\n"
                + "2. Type 'list' if you want to generate your task list.\n"
                + "3. Type 'done [number]' to mark a task as done.\n"
                + "4. Type 'delete [number]' to delete a task.\n"
                + "5. Type 'bye' to exit");
        while (true) {
            try {
                input = reader.nextLine();
                Scanner line = new Scanner(input);
                String firstWord = line.next();
                if (input.equals("bye")) {
                    break;
                } else if (firstWord.equals("done")) {
                    if (!line.hasNextInt()) {
                        throw new InvalidTaskNumberException("OOPS!!! Please specify the task "
                                + "that needs to be marked as done!");
                    }
                    int index = line.nextInt() - 1;
                    if (index >= items.size() || index < 0 || items.get(index) == null) {
                        throw new InvalidTaskNumberException("OOPS!!! Please enter a valid task number!");
                    }
                    items.get(index).setState(true);
                    System.out.println("Okay!! I have marked this task as done:\n" + items.get(index));
                } else if (firstWord.equals("delete")) {
                    if (!line.hasNextInt()) {
                        throw new InvalidTaskNumberException("OOPS!!! Please specify the task "
                                + "that needs to be deleted!");
                    }
                    int index = line.nextInt() - 1;
                    if (index >= items.size() || index < 0 || items.get(index) == null) {
                        throw new InvalidTaskNumberException("OOPS!!! Please enter a valid task number!");
                    } else {
                        Task temp = items.get(index);
                        items.remove(index);
                        System.out.println("Okay!! I have removed the following task:\n"
                                + temp);
                    }
                } else if (firstWord.equals("list")) {
                    if (items.size() == 0) {
                        System.out.println("You have no task!");
                    } else {
                        for (int i = 0; i < items.size(); i++) {
                            System.out.println((i + 1) + "." + items.get(i));
                        }
                    }
                } else {
                    String taskName = "";
                    switch (firstWord) {
                        case "todo":
                            if (!line.hasNext()) {
                                throw new EmptyDescriptionException("OOPS!!! Please specify the todo!!");
                            }
                            while (line.hasNext()) {
                                String currWord = line.next();
                                taskName = taskName + currWord + " ";
                            }
                            items.add(new Todo(taskName));
                            System.out.println(taskAddPrint(items));
                            break;
                        case "event":
                            if (!line.hasNext()) {
                                throw new EmptyDescriptionException("OOPS!!! Please specify the event!!");
                            }
                            while (line.hasNext()) {
                                String currWord = line.next();
                                if (currWord.charAt(0) == '/') {
                                    break;
                                }
                                taskName = taskName + currWord + " ";
                            }
                            items.add(new Event(taskName, getTimeDate(line)));
                            System.out.println(taskAddPrint(items));
                            break;
                        case "deadline":
                            if (!line.hasNext()) {
                                throw new EmptyDescriptionException("OOPS!!! Please specify the deadline!!");
                            }
                            while (line.hasNext()) {
                                String currWord = line.next();
                                if (currWord.charAt(0) == '/') {
                                    break;
                                }
                                taskName = taskName + currWord + " ";
                            }
                            items.add(new Deadline(taskName, getTimeDate(line)));
                            System.out.println(taskAddPrint(items));
                            break;
                        default:
                            throw new InvalidTypeException("OOPS!!! Please put either todo/event/deadline!");
                    }
                }
            } catch (EmptyDescriptionException exception) {
                System.out.println(exception.toString());
            } catch (InvalidTypeException exception) {
                System.out.println(exception.toString());
            } catch (InvalidTaskNumberException exception) {
                System.out.println(exception.toString());
            }
        }
        System.out.println("Bye. Hope to see you again soon!:)");
    }
}