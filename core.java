import java.sql.*;
import java.util.Scanner;

import oracle.jdbc.*;
import java.math.*;
import java.io.*;
import java.awt.*;
import oracle.jdbc.pool.OracleDataSource;
import sun.security.util.Password;
import java.io.Console;
public class core {
	
	
	
	static Scanner sc = new Scanner(System.in);
	static core c = new core();
	
	

   public static void main (String args []) throws SQLException {
    try{
    	
	   System.out.println("Enter username");					//input user credentials
		String usern = sc.nextLine();
		System.out.println("Enter password");
		char pass[] = Password.readPassword(System.in);
		
		String passn= new String(pass);
    	
		OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource(); //establish database connection
		ds.setURL("jdbc:oracle:thin:@127.0.0.1:1521:XE");
		Connection conn = ds.getConnection(usern, passn);
    	c.main_menu(conn);	
    	 conn.close();
    }
    catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
    catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
    }
   
  
 
   void main_menu(Connection conn) throws SQLException{			//main method to define the Menu
	   int choice = 0;
	   do{
		System.out.println("1.Display tables"); //q2
		System.out.println("2.Show classes taken by student");//q3
		System.out.println("3.Show pre-requisite of a course");//q4
		System.out.println("4.Give Class details");//q5
		System.out.println("5.Enroll student in class");//q6
		System.out.println("6.Drop student from class");//q7
		System.out.println("7.Delete Student");//q8
		System.out.println("8.Exit");
		System.out.println("Select your choice");
		choice  =sc.nextInt();
		
		switch(choice){
		case 1:c.display_options(conn);
		break;
		case 2:c.stud_courses(conn);//call proc3
		break;
		case 3:c.prereq_courses(conn);//call proc4
		break;
		case 4:c.classes_stud(conn);//call proc5
		break;
		case 5:c.enroll_stud(conn);//call proc6
		break;
		case 6:c.drop_stud_class(conn);//call proc7
		break;
		case 7:c.drop_student(conn);//call proc8
		break;
		case 8:System.exit(1);
		
		default: System.out.println("Invlaid option");
		
		} }while(choice!=8);
	}
	 void display_options(Connection conn) throws SQLException{			//sub main menu method to define sub menu
		 System.out.println("1.Students");
		 System.out.println("2.Courses");
		 System.out.println("3.Course_credit");
		 System.out.println("4.Classes");
		 System.out.println("5.Enrollments");
		 System.out.println("6.Grades");
		 System.out.println("7.Pre-requisites");
		 System.out.println("8.Logs");
		 System.out.println("9.Back to main menu");
		 int choice  =sc.nextInt();
		
		 switch(choice){
			case 1:c.show_students(conn);//call students_display prc
			break;
			case 2:c.show_courses(conn);//call courses
			break;
			case 3:c.show_course_credit(conn);//call course_credit
			break;
			case 4:c.show_classes(conn);//call classes
			break;
			case 5:c.show_enrollments(conn);//call enrollments
			break;
			case 6:c.show_grades(conn);//call grades
			break;
			case 7:c.show_Prerequisites(conn);//call prerequisites
			break;
			case 8:c.show_logs(conn); //call logs
			break;
			case 9:c.main_menu(conn);
			
			default: c.main_menu(conn);
		 } 
   
	 }
  
    	
    	
		void show_students(Connection conn) throws SQLException{		//method to shoe students table
			
		   
        //Prepare to call stored procedure:
        CallableStatement cs = conn.prepareCall("begin ? := proj2.getstudents(); end;");
        
	   //register the out Sys_refcursor parameter 
        cs.registerOutParameter(1, OracleTypes.CURSOR);
        
        
        // execute and retrieve the result set
        cs.execute();
        ResultSet rs = (ResultSet)cs.getObject(1);
        System.out.println("StudentResults");
        System.out.println("B#\tfirstname\tlastname\tstatus\t\tgpa\temail\t\t\tbdate\t\t\t\tdeptname\n------------------------------------------------------------------------------------------------------------------------");

        // print the results
        while (rs.next()) { 
            System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t\t" + rs.getString(3) + "\t\t"+ rs.getString(4) + "\t\t" + rs.getString(5) + "\t" +rs.getString(6)+"\t\t"+rs.getString(7)+"\t"+rs.getString(8)+"\n");
        }

        //close the statement
        cs.close();
		}
        
		void show_courses(Connection conn) throws SQLException{		//method to show courses table
        CallableStatement cs1 = conn.prepareCall("begin ? := proj2.getcourses(); end;");
        
 	   
         cs1.registerOutParameter(1, OracleTypes.CURSOR);
         
         
         
         cs1.execute();
         ResultSet rs1 = (ResultSet)cs1.getObject(1);

        System.out.println("\nCourse Results\n");
        System.out.println("dept_code \tcourse# \ttitle\n-----------------------------------------");
         while (rs1.next()) {
             System.out.println(rs1.getString(1) + "\t\t" +
                 rs1.getString(2) + "\t\t" + rs1.getString(3)+"\n");
         }

         //close statement
         cs1.close();
		}
		
		void show_course_credit(Connection conn) throws SQLException{		//method to show course credit table
	        CallableStatement cs1 = conn.prepareCall("begin ? := proj2.getcoursecredits(); end;");
	        
	 	   
	         cs1.registerOutParameter(1, OracleTypes.CURSOR);
	         
	         
	         
	         cs1.execute();
	         ResultSet rs1 = (ResultSet)cs1.getObject(1);

	        System.out.println("\nCourse Credit Results");
	        System.out.println("course# \t credits\n-----------------------");
	         while (rs1.next()) {
	             System.out.println(rs1.getString(1) + "\t\t" +
	                 rs1.getString(2)+"\n");
	         }

	         //close the statement
	         cs1.close();
			}
		
		void show_classes(Connection conn) throws SQLException{	//method to show classes table
	        CallableStatement cs1 = conn.prepareCall("begin ? := proj2.getclasses(); end;");
	        
	 	   
	         cs1.registerOutParameter(1, OracleTypes.CURSOR);
	         
	         
	         
	         cs1.execute();
	         ResultSet rs1 = (ResultSet)cs1.getObject(1);

	        System.out.println("\nClasses Results\n");
	        System.out.println("classid \tdept_code \tcourse# \tsect# \tyear \tsemester \tlimit \tclass_size\n---------------------------------------------------------------------------------------------------------");
	         while (rs1.next()) {
	             System.out.println(rs1.getString(1) + "\t\t" +
	                 rs1.getString(2) + "\t\t" + rs1.getString(3)+ "\t\t" + rs1.getString(4)+ "\t" + rs1.getString(5)+ "\t" + rs1.getString(6)+ "\t\t" + rs1.getString(7)+ "\t" + rs1.getString(8)+"\n");
	         }

	         //close statement
	         cs1.close();
			}
		
		void show_enrollments(Connection conn) throws SQLException{		//method to show enrollments table
	        CallableStatement cs1 = conn.prepareCall("begin ? := proj2.getenrollments(); end;");
	        
	 	   
	         cs1.registerOutParameter(1, OracleTypes.CURSOR);
	         
	         
	         
	         cs1.execute();
	         ResultSet rs1 = (ResultSet)cs1.getObject(1);

	        System.out.println("\nEnrollments Results\n");
	        System.out.println("B# \tclassid \tlgrade\n-------------------------------");
	         while (rs1.next()) {
	             System.out.println(rs1.getString(1) + "\t" +
	                 rs1.getString(2) + "\t\t" + rs1.getString(3)+"\n");
	         }

	         //close statement
	         cs1.close();
			}
		
		void show_grades(Connection conn) throws SQLException{		//method to show grades table
	        CallableStatement cs1 = conn.prepareCall("begin ? := proj2.getgrades(); end;");
	        
	 	   
	         cs1.registerOutParameter(1, OracleTypes.CURSOR);
	         
	         
	         
	         cs1.execute();
	         ResultSet rs1 = (ResultSet)cs1.getObject(1);

	        System.out.println("\nGrade Results\n");
	        System.out.println("lgrade \tngrade\n------------------------");
	         while (rs1.next()) {
	             System.out.println(rs1.getString(1) + "\t" +
	                 rs1.getString(2)+"\n");
	         }

	         //close  statement
	         cs1.close();
			}
		
		
		void show_Prerequisites(Connection conn) throws SQLException{	//method to show prerequisite table
	        CallableStatement cs1 = conn.prepareCall("begin ? := proj2.getprerequisites(); end;");
	        
	 	   
	         cs1.registerOutParameter(1, OracleTypes.CURSOR);
	         
	         
	         
	         cs1.execute();
	         ResultSet rs1 = (ResultSet)cs1.getObject(1);

	        System.out.println("\nPrerequisite Results\n");
	        System.out.println("dept_code \tcourse# \tpre_dept_code \tpre_course#\n--------------------------------------------------------------");
	         while (rs1.next()) {
	             System.out.println(rs1.getString(1) + "\t\t" +
	                 rs1.getString(2) + "\t\t" + rs1.getString(3)+ "\t\t" + rs1.getString(4)+"\n");
	         }

	         //close statement
	         cs1.close();
			}
		
		void show_logs(Connection conn) throws SQLException{		//method to show logs table
	        CallableStatement cs1 = conn.prepareCall("begin ? := proj2.getlogs(); end;");
	        
	 	   
	         cs1.registerOutParameter(1, OracleTypes.CURSOR);
	         
	         
	         
	         cs1.execute();
	         ResultSet rs1 = (ResultSet)cs1.getObject(1);

	        System.out.println("\nLog Results\n");
	        System.out.println("logid \twho \ttime \t\t\ttable_name \toperation \tkey_value\n------------------------------------------------------------------------------------------------------------------");
	         while (rs1.next()) {
	             System.out.println(rs1.getString(1) + "\t" +
	                 rs1.getString(2) + "\t" + rs1.getString(3)+ "\t" + rs1.getString(4)+ "\t" + rs1.getString(5)+ "\t\t" + rs1.getString(6)+"\n");
	         }

	         //close statement
	         cs1.close();
			}
		
//proc3		
		void stud_courses(Connection conn) throws SQLException{			//method to execute procedure 3 and display results
			
		System.out.println("Enter B#");
		String bno = sc.next().toUpperCase();
			
         CallableStatement cs2 = conn.prepareCall("begin proj2.stud_courses(?,?,?); end;");
         
   	   
         cs2.registerOutParameter(1, OracleTypes.CURSOR);				//register data types and pass input variables
         cs2.setString(2, bno);
         cs2.registerOutParameter(3, OracleTypes.NUMBER);
         
         
         
        cs2.execute();
         ResultSet rs2 = (ResultSet)cs2.getObject(1);
         
         Object msg = cs2.getObject(3);
         
       
        
         
        if(msg.toString().equalsIgnoreCase("1")){		//if no such course found
        	if(!rs2.isBeforeFirst()){
        		System.out.println("Student is not enrolled in any class");
        	}
        	else{										//else fetch and print the results
        		System.out.println("Classid \tDept_Code \tcourse# \tsection \tyear \tsemester \tlgrade \tngrade\n---------------------------------------------------------------------------------------------------------");
        		while (rs2.next()) {
        			System.out.println(rs2.getString(1) + "\t\t" +
        			rs2.getString(2) + "\t\t" + rs2.getString(3) +"\t\t" + rs2.getString(4)+"\t\t" +  rs2.getString(5)+ "\t"+ rs2.getString(6)+ "\t\t" + rs2.getString(7)+ "\t" +rs2.getString(8)+"\n");
       
        		}
        	}
        }
        else {
        	
        	 System.out.println("B# is invalid");
        	}
        cs2.close();
		}
        
//proc 4
		
        void prereq_courses(Connection conn) throws SQLException{  //method to execute procedure 4 and display results
        	
        	System.out.println("Enter Course Department");
    		String course_dpt = sc.next();
    		System.out.println("Enter course number");
    		String courseno = sc.next();
    		
        CallableStatement cs4 = conn.prepareCall("begin ? := proj2.prereq_courses(?,?); end;");
        cs4.registerOutParameter(1, OracleTypes.CURSOR);		//register data types and pass input variables
        cs4.setString(2, course_dpt);
        cs4.setString(3, courseno);
        
        cs4.execute();  										//execute statement and print results
        ResultSet rs4 = (ResultSet)cs4.getObject(1);
        System.out.println("All Prerequisite Courses\n---------------");
        while (rs4.next()) {
			System.out.println(rs4.getString(1) );
        }
        System.out.println();
        cs4.close();
        }
       
        
        
 //proc 5
        
        void classes_stud(Connection conn) throws SQLException{  //method to execute procedure 5 and display results
        	System.out.println("Enter classid");
        	String classi = sc.next().toLowerCase();
        CallableStatement cs5 = conn.prepareCall("begin ? := proj2.classes_stud(?,?); end;");
        cs5.registerOutParameter(1, OracleTypes.CURSOR);		//register data types and pass input variables
        cs5.setString(2, classi);
        cs5.registerOutParameter(3, OracleTypes.NUMBER);
        
        
        cs5.execute();  										//execute the method and print results
        ResultSet rs5 = (ResultSet)cs5.getObject(1);
        Object msg5 = cs5.getObject(3);
        if(msg5.toString().equalsIgnoreCase("1")){
        	if(!rs5.isBeforeFirst()){
        		System.out.println("no students has enrolled in the class");
        	}
        	else{
        		System.out.println("Classid \tCourse Title \t\tB# \tFirst Name\n----------------------------------------------------------------------------------------------------");
        		while (rs5.next()) {
        		System.out.println(rs5.getString(1) + "\t\t"+rs5.getString(2) + "\t\t"+rs5.getString(3) + "\t"+rs5.getString(4) );
        		}
        	}	
        }
        else{
        	System.out.println("Invalid classid");
        }
        cs5.close();
        }
        
     //proc6
        void enroll_stud(Connection conn) throws SQLException{			//method to execute proc 6
        	
        	//get input from user
        	System.out.println("Enter b#");
        	String bno = sc.next().toUpperCase();
        	
        	System.out.println("Enter classid");
        	String classi = sc.next().toLowerCase();
        CallableStatement cs6 = conn.prepareCall("begin proj2.enroll_stud(?,?,?,?,?,?,?,?,?,?); end;");
        
        
        cs6.setString(1, bno);										//pass input variables and register data types to output variables
        cs6.setString(2, classi);
        cs6.registerOutParameter(3, OracleTypes.NUMBER);
        cs6.registerOutParameter(4, OracleTypes.NUMBER);
        cs6.registerOutParameter(5, OracleTypes.NUMBER);
        cs6.registerOutParameter(6, OracleTypes.NUMBER);
        cs6.registerOutParameter(7, OracleTypes.NUMBER);
        cs6.registerOutParameter(8, OracleTypes.NUMBER);
        cs6.registerOutParameter(9, OracleTypes.NUMBER);
        cs6.registerOutParameter(10,OracleTypes.NUMBER);
        
        cs6.execute();  
         
        Object studcount = cs6.getObject(3);						//get output variables from stored proc and store into java code
        Object classcount = cs6.getObject(4);
        Object clsize= cs6.getObject(5);
        Object cllimit= cs6.getObject(6);
        Object inclass= cs6.getObject(7);
        Object clenrolled= cs6.getObject(8);
        Object prerqcount= cs6.getObject(9);
        Object isprerequisite= cs6.getObject(10);
        
        int studcount1 = Integer.parseInt(studcount.toString());		//convert object variables to string and then integer
        int classcount1 = Integer.parseInt(classcount.toString());
        int clsize1 = Integer.parseInt(clsize.toString());
        int cllimit1 = Integer.parseInt(cllimit.toString());
        int inclass1 = Integer.parseInt(inclass.toString());
        int clenrolled1 = Integer.parseInt(clenrolled.toString());
        int prerqcount1 = Integer.parseInt(prerqcount.toString());
        int isprerequisite1 = Integer.parseInt(isprerequisite.toString());
        
        if(studcount1 > 0  && classcount1 > 0){						//based on output variables design of decision
        	if(clsize1 < cllimit1){
        		if(inclass1 == 0){
        			if(isprerequisite1 == 0){
        				if(clenrolled1 == 3){
        					System.out.println("You are overloaded.");
						//insert into enrollments("B#", "CLASSID", "LGRADE") values(bno, clid, null);
        				}
        				else if (clenrolled1 > 4){
							System.out.println("Students cannot be enrolled in more than four classes in the same semester.");
        				}
        			}
        			else{
        				if(prerqcount1 ==0){
        					System.out.println("Prerequisite not satisfied.");
        				}
        				else{
        					if(clenrolled1 ==3){
        						System.out.println("You are overloaded.");
							//insert into enrollments("B#", "CLASSID", "LGRADE") values(bno, clid, null);
        					}
        					else if (clenrolled1 > 4){
							System.out.println("Students cannot be enrolled in more than four classes in the same semester.");
        					}
						}
					}
        		}
				else{
					System.out.println("Student already enrolled in class");
					}
        		}
        	else{
			System.out.println("Class is full");
        	}
        	}
        else{	
        	if ((studcount1 < 1) && (classcount1 < 1)){
        		System.out.println("The B# and classid are invalid.");
        	}
        	else{
        		if ((studcount1 < 1) && (classcount1 > 0)){
					System.out.println("The B# is invalid.");
        		}
        		else{
					System.out.println("The classid is invalid.");
        		}	
        	}
        	
        }
        cs6.close();
        }
        
        void drop_stud_class(Connection conn) throws SQLException{				//method to execute proc 7
        	System.out.println("Enter b#");									//get input from user
        	String bno = sc.next().toUpperCase();
        	
        	System.out.println("Enter classid");
        	String classi = sc.next().toLowerCase();
        //proc 7
        CallableStatement cs7 = conn.prepareCall("begin proj2.drop_stud_class(?,?,?,?,?,?,?,?); end;"); 
       
        cs7.setString(1, bno);											//register data types and pass input variables to statement
        cs7.setString(2, classi);
        cs7.registerOutParameter(3, OracleTypes.NUMBER);
        cs7.registerOutParameter(4, OracleTypes.NUMBER);
        cs7.registerOutParameter(5, OracleTypes.NUMBER);
        cs7.registerOutParameter(6, OracleTypes.NUMBER);
        cs7.registerOutParameter(7, OracleTypes.NUMBER);
        cs7.registerOutParameter(8, OracleTypes.NUMBER);
        
        cs7.execute();  												//execute stored proc.
        
        Object studcount2 = cs7.getObject(3);							//store output variables of stored proc into java code
        Object classcount2 = cs7.getObject(4);
        Object clsize2 = cs7.getObject(5);
        Object classenrolled2 = cs7.getObject(6);
        Object allclassenrolled2 = cs7.getObject(7);
        Object prereqinfo2 = cs7.getObject(8);
        
        int studcount3 = Integer.parseInt(studcount2.toString());		//convert object variables to integer
        int classcount3 = Integer.parseInt(classcount2.toString());
        int clsize3 = Integer.parseInt(clsize2.toString());
        int classenrolled3 = Integer.parseInt(classenrolled2.toString());
        int allclassenrolled3 = Integer.parseInt(allclassenrolled2.toString());
        int prereqinfo3 = Integer.parseInt(prereqinfo2.toString());
        
        if(studcount3>0 && classcount3>0){								//design of decision based on output variables from stored proc
        	if(classenrolled3>0){
        		if(prereqinfo3==0){
        			if(allclassenrolled3 ==1 && clsize3 ==1){
        				//delete from enrollments where B#=bno and classid= clid;
        				System.out.println("This student is not enrolled in any classes.");
        				System.out.println("The class now has no students.");
        			}
        			else if(allclassenrolled3 ==1){
						//delete from enrollments where B#=bno and classid= clid;
						System.out.println("This student is not enrolled in any classes.");
        			}
        			else if(clsize3==1){
					//delete from enrollments where B#=bno and classid= clid;
					System.out.println("The class now has no students.");
        			}
				
        			}
        		else{
        			System.out.println("The drop is not permitted because another class uses it as a prerequisite");
        		}
			}
        	else{
        		System.out.println("The student is not enrolled in the class");	
        		}
        }
        else{	
        	if ((studcount3<1) && (classcount3<1)){
				System.out.println("The B# and classid are both invalid.");
        	}
			else{
				if ((studcount3<1) && (classcount3>0)){
				System.out.println("The B# is invalid.");
				}
				else{
				System.out.println("The classid is invalid.");
				}
			}
        }
       	
        cs7.close();
        }
        
   //proc 8
        
        void drop_student(Connection conn) throws SQLException{			//method to execute proc 8
        	
        	System.out.println("Enter b#");								//get input from user
        	String bno= sc.next().toUpperCase();
        CallableStatement cs8 = conn.prepareCall("begin proj2.drop_student(?,?); end;"); //prepare statement
        
        cs8.setString(1, bno);										//pass input variables to statement and register data types
        cs8.registerOutParameter(2, OracleTypes.NUMBER);
        
        cs8.execute();  										//execute statement
        Object studcount4 = cs8.getObject(2);						//store output variable
        int studcount5 = Integer.parseInt(studcount4.toString());
        if(studcount5 ==0){										
        	System.out.println("Invalid B#");
        }
        
       
   } 


  }


