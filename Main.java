import java.util.*;
import java.time.*;
import model.*;

public class Main {
    
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        
        Librarian Obj1 = new Librarian(null, null, null, 0, null);
        Book Obj2 = new Book(null, null, null, 0, 0, null);
        Student Obj3 = new Student(null, 0, null, 0, 0, 0);
        FTransaction Obj4 = new FTransaction(null,0,0);

        System.out.println("----------------------------------------------------------------------------");
        System.out.println("                       Console Based Library System                         ");
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("----------------------------------------------------------------------------");
        boolean Login = false;
        int AChoice;
        do {
            System.out.print("Enter Admin Login Code : ");
            String Pass = sc.nextLine();
            if (Pass.equals(Obj1.Master())) {
                Login = true;
            }
            else {
                System.out.println("Incorrect Login Code Plz.. Try Again");
                System.out.println("--------------------------------------------------------------------");
            }
        } while (!Login);
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("                      Welcome to Java Library System                        ");
        System.out.println("----------------------------------------------------------------------------");
        do{
            System.out.print("1 For Librarian Login \n2 For Add Librarian \n3 For Admin Log-Out \nSelect Choice Which you Want to Do : ");
            AChoice = sc.nextInt();
            switch(AChoice){
                case 1:
                    sc.nextLine();
                    System.out.print("Enter Your Librarian ID : ");
                    String ID = sc.nextLine();
                    System.out.print("Enter Pass Key : ");
                    String CheckPass = sc.nextLine();
                    String SavedPasskey = Obj1.ReadLibData(ID);
                    
                    if (CheckPass.equals(SavedPasskey)){
                        System.out.println("              Hello! Librarian Lets Start the Task");
                        System.out.println("----------------------------------------------------------------------------");
                        int LChoice;
                        do{
                            System.out.print("1 For Add Student\n2 For Add Books \n3 For Students Access \n4 For Books Access \n5 For Exit.....\nTask to Do ...  : ");
                            LChoice = sc.nextInt();
                            switch (LChoice) {
                                case 1:
                                    Obj3.AddStudent();
                                    Obj3.StudentData();
                                    break;
                                case 2:
                                    Obj2.AddBook();
                                    Obj2.SaveBook();
                                    break;
                                case 3:
                                    System.out.println("-------------------------------------------------------------------");
                                    System.out.print("Student Enrollment No. : ");
                                    long Enroll = sc.nextLong();
                                    sc.nextLine();
                                    System.out.print("Student Pass Key : ");
                                    String CheckSPass = sc.nextLine();

                                    String SavedPass = Obj3.ReadStuData(Enroll);

                                    if (CheckSPass.equals(SavedPass)){
                                        System.out.println("              Hello! Lets Complete Students Task");
                                        System.out.println("----------------------------------------------------------------------------");
                                        int SChoice;
                                        do{
                                            System.out.print("1 For Books Issues \n2 For Books Return \n3 For Students details \n4 For Fine Submit \n5 Exit ....\nWhat to Do : ");
                                            SChoice = sc.nextInt();
                                            switch (SChoice) {
                                                case 1:
                                                    sc.nextLine();
                                                    System.out.print("Enter Book ID : ");
                                                    String CheckBook = sc.nextLine();
                                                    Obj2.ReadBookData(CheckBook);
                                                    if (Obj2.CheckAval(Obj3)){
                                                        Obj2.UpdateBook(CheckBook);
                                                        Obj2.Booksfile("Issue", ID,Obj3.getEnrollment(),CheckBook);
                                                        Obj2.AllBookData("Issue",ID, Obj3.getEnrollment());
                                                        Obj3.UpdateStudent(Enroll);
                                                        Obj3.StudentBUpdate("Issue", ID, Enroll,CheckBook,Obj2.getBName());
                                                    }
                                                    break;
                                                case 2:
                                                    sc.nextLine();
                                                    System.out.print("Enter Book ID : ");
                                                    CheckBook = sc.nextLine();
                                                    Obj2.ReadBookData(CheckBook);
                                                    LocalDate IssueDate = Obj3.CheckToFine(Enroll,CheckBook);
                                                    if(IssueDate != null){
                                                        Obj3.FineCal(IssueDate);
                                                        Obj3.RBIssue();
                                                        Obj2.Submit();
                                                        Obj3.UpdateStudent(Enroll);
                                                        Obj3.StudentBUpdate("Return", ID, Enroll, CheckBook, Obj2.getBName());
                                                        Obj2.UpdateBook(CheckBook);
                                                        Obj2.Booksfile("Return",ID,Obj3.getEnrollment(),CheckBook);
                                                        Obj2.AllBookData("Return",ID,Obj3.getEnrollment());
                                                    };
                                                    break;

                                                case 3:
                                                    Obj3.DisplayStud();
                                                    break;

                                                case 4:
                                                    sc.nextLine();
                                                    System.out.print("Enter Receipt No. : ");
                                                    String Receipt = sc.nextLine();
                                                    System.out.println("Enter Amount Based on Receipt : ");
                                                    double Amount = sc.nextDouble();
                                                    Obj3.FCollect(Amount);
                                                    Obj3.UpdateStudent(Enroll);
                                                    Obj4.AllFine(Receipt,Amount,ID,Enroll);
                                                    // Obj4.ReceiptList(Receipt);
                                                    break;

                                                default:
                                                    break;
                                            }
                                        }while(SChoice != 5);}
                                    break;
                                case 4:
                                    sc.nextLine();
                                    System.out.print("Enter Book ID : ");
                                    String CheckBook = sc.nextLine();
                                    Obj2.ReadBookData(CheckBook);
                                    Obj2.DisplayBook();
                                    break;

                                default:
                                    break;
                            }
                        }while(LChoice != 5);
                        System.out.println("Exit........");
                    }
                    else{
                        System.out.println("InCorrect Plz.. Try Again...");
                    }
                    break;
                case 2:
                    System.out.println("               Hello! Librarian Welcome to Librarian Family                 ");
                    System.out.println("----------------------------------------------------------------------------");
                    Obj1.AddLibrarian();
                    Obj1.LibData();
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.println("                      Thank You Be a Part of Our Family                      ");
                    System.out.println("-----------------------------------------------------------------------------");
                    break;
                case 3:
                    System.out.println("Exit....");
                    break;
                default:
                    break;
                }
            }while(AChoice != 3);
        System.out.println("Plz... Try Again");
    }
}

