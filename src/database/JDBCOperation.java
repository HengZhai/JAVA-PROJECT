package database;

import serverclient.PasswordEncryption;

import java.sql.*;

public class JDBCOperation {

    /**
     * The method is to check if the username is invalid ( if the username has already exist in the table in the database)
     * @param username username that user wants to use
     * @return a boolean value
     */
    public static boolean invalidUsername(String username) {
        Connection conn = DatabaseConnection.getConn();
        String sql = "SELECT * FROM users WHERE USERNAME = ?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConn(conn);
        }
        return false;
    }

    /**
     * The method is to create a new student account
     * @param username user-provided username
     * @param firstName user-provided firstName
     * @param lastName user-provided lastName
     * @param password user-provided password
     * @param universityID user-provided universityID
     * @param email user-provided email address
     * @param courseID courseID that only students need to choose
     */
    public static void insertStudent(String username, String firstName, String lastName, String password, String universityID, String email, int courseID) {
        Connection conn = DatabaseConnection.getConn();
        int userID;
        String sql1 = "INSERT INTO users(username, first_name, last_name, password, university_id, email) VALUES(?,?,?,?,?,?)";
        String sql2 = "INSERT INTO student(user_id, course_id) VALUES(?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, password);
            ps.setString(5, universityID);
            ps.setString(6, email);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()) {
                userID = rs.getInt(1);
                PreparedStatement preparedStatement = conn.prepareStatement(sql2);
                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, courseID);

                preparedStatement.executeUpdate();
                System.out.println("New student account created successfully");
                preparedStatement.close();
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConn(conn);
        }
    }

    /**
     * The method is to create a new tutor account
     * @param username user-provided username
     * @param firstName user-provided firstName
     * @param lastName user-provided lastName
     * @param password user-provided password
     * @param universityID user-provided universityID
     * @param email user-provided email address
     */
    public static void insertTutor(String username, String firstName, String lastName, String password, String universityID, String email) {
        Connection conn = DatabaseConnection.getConn();
        int userID;
        String sql1 = "INSERT INTO users(username, first_name, last_name, password, university_id, email) VALUES(?,?,?,?,?,?)";
        String sql2 = "INSERT INTO tutor(user_id) VALUES(?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, password);
            ps.setString(5, universityID);
            ps.setString(6, email);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()) {
                userID = rs.getInt(1);
                PreparedStatement preparedStatement = conn.prepareStatement(sql2);
                preparedStatement.setInt(1, userID);

                preparedStatement.executeUpdate();
                System.out.println("New tutor account created successfully");
                preparedStatement.close();
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConn(conn);
        }
    }


    /**
     * The method is to check if the username and password are correct when users attempt to login
     * @param type account type (student or tutor)
     * @param username user-provided username
     * @param password user-provided password
     * @return a boolean value to determine if the login attempt is successful
     */
    public static boolean login(String type, String username, String password) {
        Connection conn = DatabaseConnection.getConn();
        String sql = null;

        if (type.equals("Student")) {
            sql = "SELECT password FROM users JOIN student on users.user_id = student.user_id WHERE username = ? ";
        } else if (type.equals("Tutor")) {
            sql = "SELECT password FROM users JOIN tutor on users.user_id = tutor.user_id WHERE username = ?";
        }

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {

                String decryptedPass = PasswordEncryption.decrypt(resultSet.getString("password"));
                System.out.println("decrypt" + decryptedPass);
                if (decryptedPass.equals(password)) {
                    return true;
                }
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }


    /**
     * The method provides the user with the function to change personal email address
     * @param username username
     * @param newEmail new email address
     */
    public static void updateEmail(String username, String newEmail) {
        Connection conn = DatabaseConnection.getConn();
        String sql = "UPDATE users SET email = ? WHERE username = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, newEmail);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();
            System.out.println("Change email address successfully");
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConn(conn);
        }
    }

    /**
     * The method is to give users a chance to change their password if they forget their own password
     * @param username username that users must provide
     * @param email email address that users must provide
     * @param newPassword new password
     * @return a boolean value to determine if this operation completed successfully
     */
    public static boolean updatePassword(String username, String email, String newPassword) {
        Connection conn = DatabaseConnection.getConn();
        String sql1 = "SELECT * FROM users WHERE username = ? AND email = ?";
        String sql2 = "UPDATE users SET password = ? WHERE username = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql1);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                PreparedStatement ps = conn.prepareStatement(sql2);
                ps.setString(1, PasswordEncryption.encrypt(newPassword));
                ps.setString(2, username);

                ps.executeUpdate();
                System.out.println("Change password successfully");
                ps.close();
                return true;
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConn(conn);
        }
        return false;
    }

    /**
     * The method is to get the slotID corresponding to different time slots.
     * @param weekday Monday - Friday
     * @param startTime start time of time slot
     * @param endTime end time of time slot
     * @return slotID
     */
    public static int getSlotID(String weekday, String startTime, String endTime) {
        Connection conn = DatabaseConnection.getConn();

        String sql = "SELECT slot_id FROM slot WHERE weekday = ? AND start_time = ? AND end_time = ?";

        int slotID = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, weekday);
            preparedStatement.setString(2, startTime);
            preparedStatement.setString(3, endTime);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                slotID = rs.getInt("slot_id");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConn(conn);
        }
        return slotID;
    }

    /**
     * The method is to get the tutorID based on the username stored in the users table.
     * @param username username of tutor
     * @return tutorID
     */
    public static int getTutorID(String username) {
        Connection conn = DatabaseConnection.getConn();

        int tutorID = 0;
        String sql = "SELECT tutor_id FROM tutor INNER JOIN users on tutor.user_id = users.user_id WHERE username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tutorID = rs.getInt("tutor_id");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tutorID;
    }

    /**
     * The method is to get the studentID based on the username stored in the users table.
     * @param username username of student
     * @return studentID
     */
    public static int getStudentID(String username) {
        Connection conn = DatabaseConnection.getConn();

        int studentID = 0;
        String sql = "SELECT student_id FROM student INNER JOIN users on student.user_id = users.user_id WHERE username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                studentID = rs.getInt("student_id");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentID;
    }

    /**
     * The method is used to create time slots just for tutor accounts.
     * @param weekday Monday - Friday
     * @param startTime start time of time slot
     * @param endTime end time of time slot
     * @param username username of tutor
     * @return a boolean value to determine if this operation completed successfully
     */
    public static boolean insertBookSlot(String weekday, String startTime, String endTime, String username) {
        int slotID = getSlotID(weekday, startTime, endTime);
        int tutorID = getTutorID(username);
        Connection conn = DatabaseConnection.getConn();
        String sql1 = "SELECT slot_id, tutor_id FROM bookslot WHERE slot_id = ? AND tutor_id = ?";
        String sql2 = "INSERT INTO BOOKSLOT(slot_id, tutor_id) VALUES(?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql1);
            ps.setInt(1, slotID);
            ps.setInt(2, tutorID);

            ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                PreparedStatement preparedStatement = conn.prepareStatement(sql2);
                preparedStatement.setInt(1, slotID);
                preparedStatement.setInt(2, tutorID);

                preparedStatement.executeUpdate();
                preparedStatement.close();
                return true;
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * The method is to check whether the time slot has been booked.
     * @param slotID slotID corresponding to different time slots
     * @param tutorID tutorID corresponding to different tutors
     * @return a boolean value
     */
    public static boolean checkIsBooked(int slotID, int tutorID) {
        Connection conn = DatabaseConnection.getConn();
        boolean check = false;
        String sql = "SELECT isbooked FROM bookslot WHERE slot_id = ? AND tutor_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, slotID);
            ps.setInt(2, tutorID);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                check = rs.getBoolean("isbooked");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }

    /**
     * The method is to get courseID of student
     * @param username username of student
     * @return courseID
     */
    public static int getCourseID(String username) {
        Connection conn = DatabaseConnection.getConn();
        int courseID = 0;
        String sql = "SELECT course_id FROM student INNER JOIN users on student.user_id = users.user_id WHERE username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseID = rs.getInt("course_id");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseID;
    }

    /**
     * The method is to get all modules in the course
     * @param username username of student
     * @return a string that contains all modules
     */
    public static StringBuffer getModules(String username) {
        Connection conn = DatabaseConnection.getConn();
        int courseID = getCourseID(username);
        StringBuffer stringBuffer = new StringBuffer();
        String sql = "SELECT module_name FROM module INNER JOIN modulecourse on module.module_id = modulecourse.module_id WHERE course_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, courseID);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stringBuffer.append(rs.getString("module_name")).append(",");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /**
     * The method is to get all the free time slots that were created by tutor
     * @param username username of tutor
     * @return a string that contains all free time slots
     */
    public static StringBuffer getFreeTimeSlots(String username) {
        Connection conn = DatabaseConnection.getConn();
        int tutorID = getTutorID(username);
        StringBuffer stringBuffer = new StringBuffer();
        String sql = "SELECT weekday, start_time, end_time From slot INNER JOIN bookslot on slot.slot_id = bookslot.slot_id WHERE tutor_id = ? AND isbooked = false";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tutorID);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stringBuffer.append(rs.getString("weekday")).append(" ").append(rs.getString("start_time")).append(" ")
                .append(rs.getString("end_time")).append(",");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /**
     * The method is to get all the booked time slots that can be seen in the meeting just for student
     * @param tutorName name of tutor
     * @param studentName name of student
     * @return a string that contains all the booked time slots
     */
    public static StringBuffer getBookedTimeSlots(String tutorName, String studentName) {
        Connection conn = DatabaseConnection.getConn();
        int tutorID = getTutorID(tutorName);
        int studentID = getStudentID(studentName);
        StringBuffer stringBuffer = new StringBuffer();
        String sql = "SELECT weekday, start_time, end_time From slot INNER JOIN bookslot on slot.slot_id = bookslot.slot_id WHERE tutor_id = ? AND student_id = ? AND isbooked = true";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tutorID);
            ps.setInt(2, studentID);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stringBuffer.append(rs.getString("weekday")).append(" ").append(rs.getString("start_time")).append(" ")
                        .append(rs.getString("end_time")).append(",");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /**
     * The method is used to delete free time slot just for tutor account
     * @param weekday Monday - Friday
     * @param startTime start time of time slot
     * @param endTime end time of time slot
     * @param tutorName username of tutor
     */
    public static boolean deleteFreeTimeSlot(String weekday, String startTime, String endTime, String tutorName) {
        Connection conn = DatabaseConnection.getConn();
        int slotID = getSlotID(weekday, startTime, endTime);
        int tutorID = getTutorID(tutorName);
        boolean checkIsBooked = checkIsBooked(slotID, tutorID);
        String sql = "DELETE FROM bookslot Where slot_id = ? AND tutor_id = ?";

        try {
            if(!checkIsBooked) {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, slotID);
                ps.setInt(2, tutorID);

                ps.executeUpdate();
                System.out.println("Delete time slot successfully");
                ps.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DatabaseConnection.closeConn(conn);
        }
        return false;
    }

    /**
     * The method is to get all the username of tutors
     * @return a string that contains all the username of tutors
     */
    public static String getAllTutors() {
        Connection conn = DatabaseConnection.getConn();
        StringBuilder stringBuilder = new StringBuilder();
        String sql = "SELECT username FROM users INNER JOIN tutor on users.user_id = tutor.user_id";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stringBuilder.append(rs.getString("username")).append(",");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * The method is to get all the username of students
     * @return a string that contains all the username of students
     */
    public static String getAllStudents() {
        Connection conn = DatabaseConnection.getConn();
        StringBuilder stringBuilder = new StringBuilder();
        String sql = "SELECT username FROM users INNER JOIN student on users.user_id = student.user_id";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stringBuilder.append(rs.getString("username")).append(",");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * The method is to provide one of the most important functions that students can book the free time slots after created by tutors.
     * @param weekday Monday - Friday
     * @param startTime start time of time slot
     * @param endTime end time of time slot
     * @param tutorName name of tutor
     * @param studentName name of student
     */
    public static boolean bookTimeSlot(String weekday, String startTime, String endTime, String tutorName, String studentName) {
        Connection conn = DatabaseConnection.getConn();
        int slotID = getSlotID(weekday, startTime, endTime);
        int tutorID = getTutorID(tutorName);
        int studentID = getStudentID(studentName);
        boolean checkIsBooked = checkIsBooked(slotID, tutorID);
        String sql = "UPDATE bookslot SET student_id = ?, isbooked = true WHERE slot_id = ? AND tutor_id = ?";
        try {
            if(!checkIsBooked) {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, studentID);
                ps.setInt(2, slotID);
                ps.setInt(3, tutorID);

                ps.executeUpdate();
                ps.close();
                System.out.println("book this time slot successfully");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * The method is to get all the booked time slots that can be seen in the meeting just for tutor
     * @param tutorName name of tutor
     * @return a string that contains all the booked time slots
     */
    public static String getBookedTimeSlotsForTutor(String tutorName) {
        Connection conn = DatabaseConnection.getConn();
        int tutorID = getTutorID(tutorName);
        StringBuilder stringBuilder = new StringBuilder();
        String sql = "SELECT weekday, start_time, end_time, student_id From slot INNER JOIN bookslot on slot.slot_id = bookslot.slot_id WHERE tutor_id = ? AND isbooked = true";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, tutorID);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stringBuilder.append(rs.getString("weekday")).append(" ").append(rs.getString("start_time")).append(" ")
                        .append(rs.getString("end_time")).append(" ").append(rs.getInt("student_id")).append(",");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * The method is to get the username of the student based on the studentID
     * @param studentID studentID
     * @return username of student
     */
    public static String getStudentName(int studentID) {
        Connection conn = DatabaseConnection.getConn();

        String studentName = null;
        String sql = "SELECT username FROM users INNER JOIN student on users.user_id = student.user_id WHERE student_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentID);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                studentName = rs.getString("username");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentName;

    }

    /**
     * The method is to get the userID of the user based on the username
     * @param username username of users
     * @return userID
     */
    public static int getUserID(String username) {
        Connection conn = DatabaseConnection.getConn();
        int userID = 0;
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userID = rs.getInt("user_id");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userID;
    }

    /**
     * The method is to get the username of the user based on the userID
     * @param userID userID
     * @return username
     */
    public static String getUsername(int userID) {
        Connection conn = DatabaseConnection.getConn();
        String userName = null;
        String sql = "SELECT username FROM users WHERE user_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userName = rs.getString("username");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userName;
    }

    /**
     * The method is to get the current time
     * @return current time (yyyy-MM-dd)
     */
    public static Date getCurrentTime() {
        Connection conn = DatabaseConnection.getConn();
        Date currentTime = null;
        String sql = "SELECT current_date";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                currentTime = rs.getDate("date");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currentTime;
    }

    /**
     * The method is used to achieve the function of sending feedback.
     * @param sender username of sender
     * @param receiver username of receiver
     * @param feedback content of feedback
     */
    public static void sendFeedback(String sender, String receiver, String feedback) {
        Connection conn = DatabaseConnection.getConn();
        int senderID = getUserID(sender);
        int receiverID = getUserID(receiver);
        Date date = getCurrentTime();

        String sql = "INSERT INTO feedback(feedback_sender, feedback_receiver, datesent, feedback) VALUES (?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, senderID);
            ps.setInt(2, receiverID);
            ps.setDate(3, date);
            ps.setString(4, feedback);

            ps.executeUpdate();
            ps.close();
            System.out.println("send feedback to " + receiver + " successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method is used to achieve the function of receiving feedback
     * @param receiver username of receiver
     * @return a string that contains all the information, including username of sender, date of sent and content of feedback.
     */
    public static String getFeedback(String receiver) {
        Connection conn = DatabaseConnection.getConn();
        int receiverID = getUserID(receiver);
        StringBuilder stringBuilder = new StringBuilder();

        String sql = "SELECT feedback_sender, datesent, feedback FROM feedback WHERE feedback_receiver = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, receiverID);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stringBuilder.append(getUsername(rs.getInt("feedback_sender"))).append("/").append(rs.getDate("datesent"))
                        .append("/").append(rs.getString("feedback")).append("#");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * The method is to get all the feedback history
     * @param username username
     * @return a string that contains all the information, including username of sender, username of receiver, date of sent and content of feedback.
     */
    public static String getFeedbackHistory(String username) {
        Connection conn = DatabaseConnection.getConn();
        int userID = getUserID(username);
        StringBuilder stringBuilder = new StringBuilder();

        String sql = "SELECT feedback_sender, feedback_receiver, datesent, feedback FROM feedback WHERE feedback_sender = ? OR feedback_receiver = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, userID);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stringBuilder.append(getUsername(rs.getInt("feedback_sender"))).append("/").append(getUsername(rs.getInt("feedback_receiver"))).append("/")
                        .append(rs.getDate("datesent")).append("/").append(rs.getString("feedback")).append("#");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * The method is to get the moduleID based on the name of module
     * @param module name of module
     * @return moduleID
     */
    public static int getModuleID(String module) {
        Connection conn = DatabaseConnection.getConn();
        int moduleID = 0;
        String sql = "SELECT module_id FROM module WHERE module_name = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, module);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                moduleID = rs.getInt("module_id");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moduleID;
    }

    /**
     * The method is to build the relationship among student, module and tutor.
     * @param studentName name of student
     * @param module name of module
     * @param tutorName name of tutor
     */
    public static void insertRelationship(String studentName, String module, String tutorName) {
        Connection conn = DatabaseConnection.getConn();
        int studentID = getStudentID(studentName);
        int moduleID = getModuleID(module);
        int tutorID = getTutorID(tutorName);

        String sql1 = "SELECT student_id, module_id FROM studenttutormodule WHERE student_id = ? AND module_id = ?";
        String sql2 = "INSERT INTO studenttutormodule(student_id, module_id, tutor_id) VALUES(?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql1);
            ps.setInt(1, studentID);
            ps.setInt(2, moduleID);

            ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                PreparedStatement preparedStatement = conn.prepareStatement(sql2);
                preparedStatement.setInt(1, studentID);
                preparedStatement.setInt(2, moduleID);
                preparedStatement.setInt(3, tutorID);

                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
