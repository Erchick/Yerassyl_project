import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WeatherAppGui extends JFrame {
    private JSONObject weatherData;
    private JLabel weatherConditionImage;
    private JLabel temperatureText;
    private JLabel humidityText;
    private JLabel windspeedText;

    public WeatherAppGui() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchTextField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        topPanel.add(new JLabel("Enter Location:"));
        topPanel.add(searchTextField);
        topPanel.add(searchButton);

        JPanel weatherPanel = new JPanel(new GridLayout(4, 1));
        weatherConditionImage = new JLabel();
        temperatureText = new JLabel();
        humidityText = new JLabel();
        windspeedText = new JLabel();

        weatherPanel.add(weatherConditionImage);
        weatherPanel.add(temperatureText);
        weatherPanel.add(humidityText);
        weatherPanel.add(windspeedText);

        add(topPanel, BorderLayout.NORTH);
        add(weatherPanel, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchTextField.getText().trim();
                if (userInput.isEmpty()) {
                    JOptionPane.showMessageDialog(WeatherAppGui.this, "Please enter a location.");
                } else {
                    weatherData = WeatherDataManager.fetchWeatherInfo(userInput);
                    updateWeatherInfo();
                }
            }
        });
    }

    private void updateWeatherInfo() {
        if (weatherData != null) {
            String weatherCondition = (String) weatherData.get("weather_condition");
            ImageIcon weatherIcon = loadImage("src/assets/" + weatherCondition.toLowerCase() + ".png");
            weatherConditionImage.setIcon(weatherIcon);

            double temperature = (double) weatherData.get("temperature");
            temperatureText.setText(temperature + " °C");

            long humidity = (long) weatherData.get("humidity");
            humidityText.setText("Humidity: " + humidity + "%");

            double windspeed = (double) weatherData.get("windspeed");
            windspeedText.setText("Windspeed: " + windspeed + " km/h");
        }
    }

    private ImageIcon loadImage(String resourcePath) {
        try {
            return new ImageIcon(new ImageIcon(resourcePath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WeatherAppGui().setVisible(true);
            }
        });
    }
}

