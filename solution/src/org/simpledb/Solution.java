package org.simpledb;
import java.io.*;

public class Solution {
    public static void main(String args[] ) throws Exception {
        String line;
        TransactionLayer database = new IncompleteTransactionLayer(new InMemoryDatabase());
        BufferedReader stdin =  new BufferedReader(new InputStreamReader(System.in));

        while((line = stdin.readLine()) != null) {
            Command command = CommandFactory.Parse(line);
            Response response = command.execute(database);
            database = response.database();
            String result = response.toString();
            if(!result.isEmpty()) System.out.println(result);
        }
    }
}