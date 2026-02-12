// QuickBite - Fast Food Ordering System (Enhanced Version with Contact Info)
// Developed in Java Swing with Improved UI/UX and Code Structure

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuickBiteApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuickBiteFrame());
    }
}

class QuickBiteFrame extends JFrame {
    JTabbedPane tabs;
    HomePanel home;
    OrderPanel order;
    ReceiptPanel receipt;

    public QuickBiteFrame() {
        setTitle("QuickBite - Fast Food Ordering System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs = new JTabbedPane();
        home = new HomePanel();
        order = new OrderPanel();
        receipt = new ReceiptPanel(order);

        tabs.addTab("Home", home);
        tabs.addTab("Order", order);
        tabs.addTab("Receipt", receipt);

        add(tabs);
        setVisible(true);
    }
}

class HomePanel extends JPanel {
    public HomePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Welcome to QuickBite", JLabel.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 28));

        JTextArea info = new JTextArea("QuickBite is a professional fast food ordering system designed to simplify and speed up your meal selection.\n\nMenu Highlights:\n- Burger: RM10\n- Fries: RM5\n- Coke: RM3\n- Nuggets: RM8\n\nVisit us or reach out:\nAddress: Sebrang Ramai, Jaya Fasa 03, 02000, Kuala Perlis, Perlis, Malaysia\nPhone: +60193452030\nEmail: quickbite@gmail.com\nFacebook: QuickBite\nInstagram: QuickBite\nTwitter: QuickBite");
        info.setFont(new Font("SansSerif", Font.PLAIN, 16));
        info.setEditable(false);
        info.setBackground(new Color(250, 250, 250));

        ImageIcon icon = new ImageIcon("/mnt/data/burger.jpg");
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(title, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
        add(info, BorderLayout.SOUTH);
    }
}

class OrderPanel extends JPanel {
    Map<String, Double> menuItems;
    Map<String, JSpinner> quantitySpinners;
    JButton submitBtn;
    Map<String, Integer> currentOrder;
    double total;

    public OrderPanel() {
        menuItems = new LinkedHashMap<>();
        quantitySpinners = new LinkedHashMap<>();
        currentOrder = new LinkedHashMap<>();

        menuItems.put("Burger", 10.0);
        menuItems.put("Fries", 5.0);
        menuItems.put("Coke", 3.0);
        menuItems.put("Nuggets", 8.0);

        setLayout(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(menuItems.size() + 1, 4, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Item"));
        formPanel.add(new JLabel("Price (RM)"));
        formPanel.add(new JLabel("Quantity"));
        formPanel.add(new JLabel("Image"));

        for (String item : menuItems.keySet()) {
            formPanel.add(new JLabel(item));
            formPanel.add(new JLabel(String.format("%.2f", menuItems.get(item))));
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            quantitySpinners.put(item, spinner);
            formPanel.add(spinner);
            formPanel.add(new JLabel(new ImageIcon("/mnt/data/" + item.toLowerCase() + ".jpg")));
        }

        submitBtn = new JButton("Place Order");
        submitBtn.setFont(new Font("Arial", Font.BOLD, 16));
        submitBtn.setBackground(new Color(0, 123, 255));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.addActionListener(e -> calculateOrder());

        add(formPanel, BorderLayout.CENTER);
        add(submitBtn, BorderLayout.SOUTH);
    }

    public void calculateOrder() {
        currentOrder.clear();
        total = 0;

        for (String item : menuItems.keySet()) {
            int qty = (int) quantitySpinners.get(item).getValue();
            if (qty > 0) {
                currentOrder.put(item, qty);
                total += qty * menuItems.get(item);
            }
        }

        if (currentOrder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items selected! Please choose at least one.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Order placed successfully!\nGo to the Receipt tab.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public Map<String, Integer> getOrderDetails() {
        return currentOrder;
    }

    public double getTotal() {
        return total;
    }
}

class ReceiptPanel extends JPanel {
    JTextArea receiptArea;
    JButton showBtn;
    OrderPanel orderPanel;

    public ReceiptPanel(OrderPanel orderPanel) {
        this.orderPanel = orderPanel;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(receiptArea);

        showBtn = new JButton("Show Receipt");
        showBtn.setFont(new Font("Arial", Font.BOLD, 16));
        showBtn.setBackground(new Color(40, 167, 69));
        showBtn.setForeground(Color.WHITE);
        showBtn.addActionListener(e -> displayReceipt());

        add(scroll, BorderLayout.CENTER);
        add(showBtn, BorderLayout.SOUTH);
    }

    public void displayReceipt() {
        Map<String, Integer> details = orderPanel.getOrderDetails();
        double total = orderPanel.getTotal();

        StringBuilder receipt = new StringBuilder();
        receipt.append("===========================\n");
        receipt.append("       QuickBite Receipt     \n");
        receipt.append("===========================\n");

        for (Map.Entry<String, Integer> entry : details.entrySet()) {
            String item = entry.getKey();
            int qty = entry.getValue();
            double price = qty * orderPanel.menuItems.get(item);
            receipt.append(String.format("%s x%d = RM%.2f\n", item, qty, price));
        }

        receipt.append("---------------------------\n");
        receipt.append(String.format("Total: RM%.2f\n", total));
        receipt.append("===========================\n");
        receipt.append("Thank you for ordering!\n");
        receipt.append("QuickBite\n");
        receipt.append("Sebrang Ramai, Jaya Fasa 03, 02000, Kuala Perlis, Malaysia\n");
        receipt.append("Tel: +60193452030 | Email: quickbite@gmail.com\n");
        receipt.append("Facebook | Instagram | Twitter: @QuickBite\n");

        receiptArea.setText(receipt.toString());
    }
}
