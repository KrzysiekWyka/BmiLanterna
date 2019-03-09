/**
 * Wydział Elektroniki i Informatyki - Politechnika Koszalińska
 * Developed by Krzysztof Wyka
 */
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
        // Setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        // Setup WindowBasedTextGUI for dialogs
        final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);

        // Create panel to hold components
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(3));

        // Setup firstName components
        panel.addComponent(new Label("Imie"));

        final TextBox firstNameBox = new TextBox().setValidationPattern(Pattern.compile("[^(0-9)]*"));

        panel.addComponent(firstNameBox);

        panel.addComponent(new EmptySpace(new TerminalSize(1,0)));

        // Setup age components
        panel.addComponent(new Label("Wiek"));

        final TextBox ageBox = new TextBox().setValidationPattern(Pattern.compile("[1-9][0-9]{0,2}"));

        panel.addComponent(ageBox);

        panel.addComponent(new Label("lat"));

        // Setup weight components
        panel.addComponent(new Label("Waga"));

        final TextBox weightBox = new TextBox().setValidationPattern(Pattern.compile("[1-9][0-9]{0,2}"));

        panel.addComponent(weightBox);

        panel.addComponent(new Label("kg"));

        // Setup height components
        panel.addComponent(new Label("Wzrost"));

        final TextBox heightBox = new TextBox().setValidationPattern(Pattern.compile("[1-9][0-9]{0,2}"));

        panel.addComponent(heightBox);

        panel.addComponent(new Label("cm"));

        panel.addComponent(new EmptySpace(new TerminalSize(0,0))); // Empty space underneath labels

        // Submit button
        final Button submitButton = new Button("Wylicz BMI", new Runnable() {
            @Override
            public void run() {
                String weightValue = weightBox.getText();
                String heightValue = heightBox.getText();

                if (weightValue.isEmpty() || heightValue.isEmpty()) {
                    MessageDialog.showMessageDialog(textGUI, "Błąd walidacyjny", "Aby wyliczyć twoje BMI należy wypełnić wszystkie pola.");
                } else {
                    // Calculate BMI
                    final int weight = Integer.parseInt(weightValue);
                    final int height = Integer.parseInt(heightValue);

                    final double heightInMeters = height / 100.0;

                    final double result = weight / Math.pow(heightInMeters, 2);

                    String message = String.format("Twoje imie: %s (%s). Masz %s lat.\r\nTwoja waga: %s kg, a wzrost: %s cm.\r\nTwoje BMI wynosi %.2f - %s.", firstNameBox.getText(), firstNameBox.getText().endsWith("a") ? 'K' : 'M', ageBox.getText(), weightBox.getText(), heightBox.getText(), result, translateResult(result));

                    MessageDialog.showMessageDialog(textGUI, "Twoje BMI", message);
                }
            }
        });

        panel.addComponent(submitButton);

        // Create window to hold the panel
        BasicWindow window = new BasicWindow();
        window.setComponent(panel);

        // Create gui and start gui
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindowAndWait(window);
    }

    private static String translateResult(double result) {
        if (result < 16.) return "wygłodzenie";
        if (result >= 16 && result <= 16.99) return "wychudzenie";
        if (result >= 17 && result <= 18.49) return "niedowaga";
        if (result >= 18.5 && result <= 24.99) return "wartość prawidłowa";
        if (result >= 25 && result <= 29.99) return "nadwaga";
        if (result >= 30 && result <= 34.99) return "I stopień otyłości";
        if (result >= 35 && result <= 39.99) return "II stopień otyłości";

        return "otyłość skrajna";
    }
}