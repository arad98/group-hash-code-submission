package com.company;
import java.io.*;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;


public class Main {

    public static String in = "/Users/yifengzhang/IdeaProjects/HashCode/inFiles/d_tough_choices.txt";
    public static String out = "/Users/yifengzhang/IdeaProjects/HashCode/outFiles/d_tough_choices.txt";



    public static void main(String[] args) throws Exception {
        // write your code here

        long startTime = System.currentTimeMillis();

        File file = new File(in);
        Scanner scanner = new Scanner(file);

        //Read information from input file
        int B = scanner.nextInt();  // the number of different books
        int L = scanner.nextInt();  // the number of libraries
        int D = scanner.nextInt();  // the number of days
        // System.out.println( "input:     " + B +  "  " + L + "  " + D);

        // read more information
        int[] books = new int[B]; // the number of books in library
        for (int i = 0; i < B; i++) {
            books[i] = scanner.nextInt();
        }

        List<Integer>[] lists = new ArrayList[L]; // the number of days takes to finish sign up
        for (int i = 0; i < L; i++) {
            List<Integer> list = new ArrayList<>();
            list.add(scanner.nextInt()); // add number of books in the library-i
            list.add(scanner.nextInt()); // add signup process days for library-i
            list.add(scanner.nextInt()); // add how many books can be shipped in one day
            for (int j = 0; j < list.get(0); j++) {
                list.add(scanner.nextInt()); // add the book number in library-i
            }
            lists[i] = list;
        }


        // sloution
        List<List<Integer>> answer = slution(books, lists, D);

        // Creating a File object that represents the disk file.
        PrintStream o = new PrintStream(out);

        // Store current System.out before assigning a new value
        PrintStream console = System.out;
        // Assign o to output stream
        System.setOut(o);


        for (int i = 0; i < answer.size(); i++) {
            List<Integer> list = answer.get(i);
            for (int j = 0; j < list.size(); j++) {
                System.out.print(list.get(j) + " ");
            }
            System.out.println();

        }

       //  System.out.println();

        System.setOut(console);
        // System.out.println("Elapsed time = " + (System.currentTimeMillis() - startTime) + " ms.");

    }

    private static List<List<Integer>> slution(int[] books, List<Integer>[] lists, int D ) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> orderSignedUpLibrary = new ArrayList<>();
        List<List<Integer>> scannedBookLists = new ArrayList<>();
        Map<Integer, List<Integer>> map = new HashMap<>();


        boolean[] scannedBooks = new boolean[books.length];
        int L = lists.length;

        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());

        for (int i = 0; i < L; i++) {
            int totalSocres = 0;
            int signUpDays = lists[i].get(1);
            for (int j = 3; j < lists[i].size(); j++) {
                int s = books[lists[i].get(j)];
                totalSocres += s;
            }
            // System.out.println(totalSocres);

             totalSocres /= signUpDays;
           // System.out.println(totalSocres);
            // Long x = ((long)totalSocres << 16) + (long)i;
            // System.out.println(x + "   " + i);
            if (map.containsKey(totalSocres)) {
                map.get(totalSocres).add(i);

            }else {
                pq.add(totalSocres);
                map.put(totalSocres,new ArrayList<>());
                map.get(totalSocres).add(i);
            }
            if (!map.containsKey((totalSocres))) {
                map.put(totalSocres, new ArrayList<>());
                map.get(totalSocres).add(i);
            }

            //System.out.println(map.get(totalSocres));
        }

        // System.out.println(map.size());
        // System.out.println(pq.size());

        int usedDays = 0;
        int booksScannedPerDay = 0;

        int cnt = 0;

        while (usedDays < D && !pq.isEmpty()) {
            // the #library
            cnt++;
            int x = pq.poll();


            List<Integer> ls = map.get(x);
            // System.out.println(x + " : " + ls);

            for (int b : ls) {
                //
                List<Integer> canScannedBooks = lists[b];
                List<Integer> toScan = new ArrayList<>();
                usedDays += lists[b].get(1);
                booksScannedPerDay = lists[b].get(2);
               // System.out.println("usedDays  " +  usedDays + "  D = " + D);
                if(usedDays >= D) break;
                int goToScan = 0;
                int cur = 3;
                while (usedDays + goToScan < D) {
                   // System.out.println("usedDays1  " +  usedDays + "  goToScan = " + goToScan);
                    // try to scan
                    for (int i = 0; i < booksScannedPerDay; i++) {
                        while (cur < canScannedBooks.size()) {
                            int y = canScannedBooks.get(cur);
                            if (!scannedBooks[y]) {
                                scannedBooks[y] = true;
                                toScan.add(y);
                                cur++;
                                break;
                            }
                            cur++;
                        }
                        if (cur == canScannedBooks.size()) break;
                    }

                    goToScan++;

                }
               // System.out.println("toScan : " + toScan.size());
                if (toScan.size() == 0) {
                    usedDays -= lists[b].get(1);

                }else {
                    // signup this library and scan the books in the library

                    orderSignedUpLibrary.add(b);
                    scannedBookLists.add(new ArrayList<>(toScan));
                }

            }
           // System.out.println("usedDay " + usedDays);
        }

        //System.out.println("orderL : " + orderSignedUpLibrary);

        List<Integer> totalLibraries = new ArrayList<>();
        totalLibraries.add(orderSignedUpLibrary.size());

        result.add(totalLibraries);

        for (int i = 0; i < orderSignedUpLibrary.size(); i++) {
            List<Integer> l1 = new ArrayList<>();
            l1.add(orderSignedUpLibrary.get(i));
            l1.add(scannedBookLists.get(i).size());
            // System.out.println("l1 = " + l1);
            result.add(new ArrayList<>(l1));
            result.add(scannedBookLists.get(i));

        }
        /*
        for (List<Integer> o : result) {
            System.out.println(o);
        }
        */

        int sum = 0;
        for (int i = 0; i < books.length; i++) {
            if (scannedBooks[i]) sum += books[i];
        }
        System.out.println("sum = " + sum);
        return result;

    }


}
