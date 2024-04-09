import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Online_Test extends JFrame implements ActionListener {
    private JLabel questionLabel;
    private JRadioButton[] options;
    private JButton nextButton, bookmarkButton;
    private ButtonGroup optionGroup;

    private int currentQuestion = 0;
    private int score = 0;
    private int[] correctAnswers = {3, 2, 3, 0, 1, 2, 0, 3, 3, 1}; // Correct options for each question

    private int studentID;

    private JLabel l;
    private JRadioButton[] jb;

    public Online_Test(String title) {
        super(title);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        setContentPane(panel);

        // Get Student ID
        String studentIDString = JOptionPane.showInputDialog("Enter your Student ID:");
        try {
            studentID = Integer.parseInt(studentIDString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID. Exiting...");
            System.exit(0);
        }

        l = new JLabel();
        panel.add(l);

        jb = new JRadioButton[4];
        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            jb[i] = new JRadioButton();
            optionGroup.add(jb[i]);
            panel.add(jb[i]);
        }

        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        panel.add(nextButton);

        bookmarkButton = new JButton("Bookmark");
        bookmarkButton.addActionListener(this);
        panel.add(bookmarkButton);

        setQuestion();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setQuestion() {
        l.setText("Question " + (currentQuestion + 1) + ": " + getQuestion(currentQuestion));
        String[] optionsArray = getOptions(currentQuestion);

        for (int i = 0; i < 4; i++) {
            jb[i].setText(optionsArray[i]);
            jb[i].setSelected(false);
        }
    }

    private String getQuestion(int questionNumber) {
        switch (questionNumber) {
            case 0:
                return "Which one among these is not a primitive datatype?";
            case 1:
                return "Which class is available to all the class automatically?";
            case 2:
                return "Which package is directly available to our class without importing it?";
            case 3:
                return "String class is defined in which package?";
            case 4:
                return "Is Null a keyword?";
            case 5:
                return "Which one among these is not a keyword?";
            case 6:
                return "String is mutable or not?";
            case 7:
                return "How many loops are there in Java?";
            case 8:
                return "Java is object-oriented or procedure-oriented?";
            case 9:
                return "Java's before name?";
            default:
                return "";
        }
    }

    private String[] getOptions(int questionNumber) {
        switch (questionNumber) {
            case 0:
                return new String[]{"int", "Float", "boolean", "array"};
            case 1:
                return new String[]{"Swing", "Applet", "Object", "ActionEvent"};
            case 2:
                return new String[]{"swing", "applet", "net", "lang"};
            case 3:
                return new String[]{"lang", "Swing", "Applet", "awt"};
            case 4:
                return new String[]{"keyword", "not a keyword", "sometimes", "Based on compilation"};
            case 5:
                return new String[]{"class", "int", "get", "if"};
            case 6:
                return new String[]{"immutable", "mutable", "either mutable or immutable", "neither mutable nor immutable"};
            case 7:
                return new String[]{"3", "6", "5", "4"};
            case 8:
                return new String[]{"procedure oriented", "both of them", "Not both of them", "object oriented"};
            case 9:
                return new String[]{"polo", "oak", "ion", "yen"};
            default:
                return new String[]{};
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            if (optionGroup.getSelection() != null) {
                int selectedOption = -1;
                for (int i = 0; i < 4; i++) {
                    if (jb[i].isSelected()) {
                        selectedOption = i;
                        break;
                    }
                }

                if (selectedOption == correctAnswers[currentQuestion]) {
                    score++;
                }

                currentQuestion++;

                if (currentQuestion < 10) {
                    setQuestion();
                } else {
                    showResult();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an option.");
            }
        } else if (e.getSource() == bookmarkButton) {
            // Implement bookmark functionality if needed
        }
    }

    private void showResult() {
        JOptionPane.showMessageDialog(this, "Your score is: " + score);

        // Store marks in the database
        storeMarksInDatabase(studentID, score);

        System.exit(0);
    }

    private void storeMarksInDatabase(int studentID, int marks) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Replace the following values with your database information
            String url = "jdbc:mysql://localhost:3306/marks";
            String username = "admin";
            String password = "admin123";

            conn = DriverManager.getConnection(url, username, password);

            // Replace "score_table" with your table name and "student_id" with the column name for student ID
            String sql = "INSERT INTO score (student_id, marks) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentID);
            pstmt.setInt(2, marks);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Online_Test("Online Test Of Java"));
    }
}
