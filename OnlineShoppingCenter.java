# JavaAssignmentimport javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class OnlineShoppingCenter {
    // Create a map to hold product information (name, price, and image path)
    private static final HashMap<String, Product> tshirts = new HashMap<>();
    private static final HashMap<String, Product> hats = new HashMap<>();
    private static final LinkedHashMap<String, Integer> selectedProducts = new LinkedHashMap<>();
    private static JFrame mainFrame;
    private static JPanel cardPanel;

    public static void main(String[] args) {
        // Display a welcome message when the program starts
        JOptionPane.showMessageDialog(null,
                "Welcome to Fanka's Online Shopping Center!\nExplore our collection of T-Shirts and Hats.\nClick a category to start shopping.",
                "Welcome",
                JOptionPane.INFORMATION_MESSAGE);

        // Define the path to the image folder
        String imageFolderPath = "C:\\Users\\hillarius\\Desktop\\images\\";

        // Initialize the product data
        tshirts.put("graphic tee", new Product("Graphic Tee", 9000, imageFolderPath + "graphic_tee.jpeg"));
        tshirts.put("polo shirt", new Product("Polo Shirt", 1100, imageFolderPath + "polo_shirt.jpeg"));
        tshirts.put("v neck shirt", new Product("V-Neck Shirt", 1000, imageFolderPath + "v_neck_tshirt.jpeg"));
        tshirts.put("sleeveless tee", new Product("Sleeveless Tee", 2000, imageFolderPath + "sleeveless_tee.jpeg"));

        hats.put("baseball cap", new Product("Baseball Cap", 12000, imageFolderPath + "baseball_cap.jpeg"));
        hats.put("bucket hat", new Product("Bucket Hat", 11000, imageFolderPath + "bucket_hat.jpeg"));
        hats.put("sun hat", new Product("Sun Hat", 9000, imageFolderPath + "sun_hat.jpeg"));
        hats.put("wide brim hat", new Product("Wide Brim Hat", 15000, imageFolderPath + "wide_brim_hat.jpeg"));

        // Create the main frame for category selection and product display
        mainFrame = new JFrame("Welcome to Fanka's Online Shopping Center");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLayout(new CardLayout());

        cardPanel = new JPanel(new CardLayout());

        // Category Selection Panel
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(2, 2, 20, 20)); // GridLayout for category buttons

        // T-Shirts category button
        JPanel tshirtPanel = new JPanel(new BorderLayout());
        JButton tshirtButton = new JButton(new ImageIcon(imageFolderPath + "tshirts.jpeg"));
        tshirtButton.setToolTipText("T-Shirts");
        tshirtButton.addActionListener(e -> {
            // Display welcome message for the T-shirts category
            JOptionPane.showMessageDialog(mainFrame,
                    "You are now browsing the T-Shirts section. Select a product to add to your cart.",
                    "T-Shirts",
                    JOptionPane.INFORMATION_MESSAGE);
            // Display the products for T-Shirts
            displayProducts("T-Shirts", tshirts);
        });
        JLabel tshirtLabel = new JLabel("T-Shirts", JLabel.CENTER);
        tshirtPanel.add(tshirtButton, BorderLayout.CENTER);
        tshirtPanel.add(tshirtLabel, BorderLayout.SOUTH);

        // Hats category button
        JPanel hatsPanel = new JPanel(new BorderLayout());
        JButton hatsButton = new JButton(new ImageIcon(imageFolderPath + "hat.jpeg"));
        hatsButton.setToolTipText("Hats");
        hatsButton.addActionListener(e -> {
            // Display welcome message for the Hats category
            JOptionPane.showMessageDialog(mainFrame,
                    "You are now browsing the Hats section. Select a product to add to your cart.",
                    "Hats",
                    JOptionPane.INFORMATION_MESSAGE);
            // Display the products for Hats
            displayProducts("Hats", hats);
        });
        JLabel hatsLabel = new JLabel("Hats", JLabel.CENTER);
        hatsPanel.add(hatsButton, BorderLayout.CENTER);
        hatsPanel.add(hatsLabel, BorderLayout.SOUTH);

        // Add panels to categoryPanel
        categoryPanel.add(tshirtPanel);
        categoryPanel.add(hatsPanel);

        // Add the category panel to the cardPanel
        cardPanel.add(categoryPanel, "categoryPanel");

        mainFrame.add(cardPanel);
        mainFrame.setVisible(true);
    }

    // Display products in the selected category
    private static void displayProducts(String category, HashMap<String, Product> products) {
        JFrame productFrame = new JFrame(category);
        productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productFrame.setSize(800, 600);

        JPanel productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(2, 2, 10, 10)); // Set to 2x2 grid

        JLabel costLabel = new JLabel("Total: Tsh 0.00", JLabel.CENTER);
        costLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));

        JButton payButton = new JButton("Make Payment");
        payButton.setEnabled(false);

        for (String key : products.keySet()) {
            Product product = products.get(key);

            JPanel productContainer = new JPanel();
            productContainer.setLayout(new BorderLayout(5, 5));

            JButton productButton = new JButton(new ImageIcon(product.getImagePath()));
            productButton.setToolTipText(product.getName());
            productButton.addActionListener(e -> {
                String input = JOptionPane.showInputDialog(productFrame, "How many " + product.getName() + " do you want?", "Quantity", JOptionPane.PLAIN_MESSAGE);
                if (input == null || input.isEmpty()) {
                    return;
                }

                try {
                    int quantity = Integer.parseInt(input);
                    if (quantity > 0) {
                        selectedProducts.put(key, quantity);
                    } else {
                        JOptionPane.showMessageDialog(productFrame, "Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(productFrame, "Invalid input. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                double totalCost = selectedProducts.keySet().stream()
                        .mapToDouble(k -> products.get(k).getPrice() * selectedProducts.get(k))
                        .sum();
                costLabel.setText(String.format("Total: Tsh%.2f", totalCost));
                payButton.setEnabled(!selectedProducts.isEmpty());
            });

            JLabel nameLabel = new JLabel(product.getName(), JLabel.CENTER);
            JLabel priceLabel = new JLabel(String.format("Tsh%.2f", product.getPrice()), JLabel.CENTER);

            productContainer.add(productButton, BorderLayout.CENTER);
            productContainer.add(nameLabel, BorderLayout.NORTH);
            productContainer.add(priceLabel, BorderLayout.SOUTH);

            productPanel.add(productContainer);
        }

        payButton.addActionListener(e -> processPayment(productFrame, costLabel));

        productFrame.setLayout(new BorderLayout());
        productFrame.add(new JScrollPane(productPanel), BorderLayout.CENTER);
        productFrame.add(costLabel, BorderLayout.NORTH); // Changed to NORTH for better positioning
        productFrame.add(payButton, BorderLayout.SOUTH); // Placed the button at the bottom

        productFrame.setVisible(true);
    }

    private static void processPayment(JFrame frame, JLabel costLabel) {
        String input = JOptionPane.showInputDialog(frame, "Enter the amount of money you are depositing:", "Payment", JOptionPane.PLAIN_MESSAGE);
        if (input == null || input.isEmpty()) {
            return;
        }

        try {
            double paymentAmount = Double.parseDouble(input);

            double totalCost = selectedProducts.keySet().stream()
                    .mapToDouble(k -> (tshirts.containsKey(k) ? tshirts.get(k).getPrice() : hats.get(k).getPrice()) * selectedProducts.get(k))
                    .sum();

            if (paymentAmount >= totalCost) {
                double change = paymentAmount - totalCost;

                StringBuilder receipt = new StringBuilder("Receipt:\n");
                for (String key : selectedProducts.keySet()) {
                    Product product = tshirts.containsKey(key) ? tshirts.get(key) : hats.get(key);
                    int quantity = selectedProducts.get(key);
                    double cost = product.getPrice() * quantity;
                    receipt.append(String.format("%s x%d - Tsh%.2f\n", product.getName(), quantity, cost));
                }
                receipt.append(String.format("\nTotal: Tsh%.2f\n", totalCost));
                receipt.append(String.format("Amount Paid: Tsh%.2f\n", paymentAmount));
                receipt.append(String.format("Change: Tsh%.2f", change));

                JOptionPane.showMessageDialog(frame, receipt.toString(), "Payment Successful", JOptionPane.INFORMATION_MESSAGE);

                // Thank you message after purchase
                JOptionPane.showMessageDialog(frame, "Thank you for shopping with us! We hope to see you again soon.", "Thank You", JOptionPane.INFORMATION_MESSAGE);

                selectedProducts.clear();
                costLabel.setText("Total: Tsh 0.00");
            } else {
                JOptionPane.showMessageDialog(frame, "Insufficient funds. Please deposit at least Tsh" + totalCost, "Payment Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid amount entered. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static class Product {
        private final String name;
        private final double price;
        private final String imagePath;

        public Product(String name, double price, String imagePath) {
            this.name = name;
            this.price = price;
            this.imagePath = imagePath;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
}
