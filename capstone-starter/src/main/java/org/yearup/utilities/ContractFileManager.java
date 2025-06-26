package org.yearup.utilities;

import org.yearup.models.ShoppingCartItem;
import org.yearup.models.contracts.Contract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ContractFileManager {

    public static void saveContract(Contract contract) throws IOException {
        // Klasör oluşturma
        File directory = new File("contracts");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "contract_" + contract.getCustomerName() + "_" +
                contract.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";

        try (FileWriter writer = new FileWriter("contracts/" + fileName)) {
            writer.write("Customer: " + contract.getCustomerName() + "\n");
            writer.write("Date: " + contract.getDate() + "\n");
            writer.write("Items:\n");

            for (ShoppingCartItem item : contract.getItems()) {
                writer.write("- " + item.getProduct().getName() + " x" + item.getQuantity() + "\n");
            }

            writer.write("Subtotal: $" + contract.getSubtotal() + "\n");
            writer.write("Tax: $" + contract.getTax() + "\n");
            writer.write("Total: $" + contract.getTotal() + "\n");
        }
    }
}
