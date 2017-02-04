delete from logs;
drop sequence log_sequence;
create sequence log_sequence
minvalue 1000
maxvalue 9999
start with 1000
increment by 1
nocache;

drop package proj2;
create or replace package proj2 as
    
	type ref_cursor is ref cursor;
    	
	function getstudents
    	return ref_cursor;

    	function getcourses
	return ref_cursor;

   	function getcoursecredits
	return ref_cursor;

	function getclasses
	return ref_cursor;

	function getenrollments
	return ref_cursor;

	function getgrades
	return ref_cursor;

	function getprerequisites
	return ref_cursor;

	function getlogs
	return ref_cursor;


procedure stud_courses(rc out sys_refcursor, bno in Students.B#%type, studcount out number);


function prereq_courses(depcode in Prerequisites.dept_code%type, cno in Prerequisites.course# %type)
return ref_cursor;

function classes_stud(c_cid in classes.classid%type, classcount out number)
return ref_cursor;

procedure drop_stud_class(bno in Students.B#%type, clid in Enrollments.classid%type, studcount out number, classcount out number, clsize out number, classenrolled out number, allclassenrolled out number, prereqinfo out number);

procedure enroll_stud(bno in Students.B#%type, clid in Classes.classid%type, studcount out number, classcount out number, clsize out number, cllimit out number, inclass out number, clenrolled out number, prerqcount out number, isprerequisite out number);

procedure drop_student(bno in students.b#%type, studcount out number);	
	end;
/
    show errors

create or replace package body proj2 as
    function getstudents
    return ref_cursor is
    rc ref_cursor;
    begin
    open rc for
    select * from students;
    return rc;
    end;

 function getcourses
    return ref_cursor is
    rc ref_cursor;
    begin
    open rc for
    select * from Courses;
    return rc;
    end;

function getcoursecredits
    return ref_cursor is
    rc ref_cursor;
    begin
    open rc for
    select * from Course_credit;
    return rc;
    end;

function getclasses
    return ref_cursor is
    rc ref_cursor;
    begin
    open rc for
    select * from Classes;
    return rc;
    end;

function getenrollments
    return ref_cursor is
    rc ref_cursor;
    begin
    open rc for
    select * from Enrollments;
    return rc;
    end;

function getgrades
    return ref_cursor is
    rc ref_cursor;
    begin
    open rc for
    select * from Grades;
    return rc;
    end;

function getprerequisites
    return ref_cursor is
    rc ref_cursor;
begin
    open rc for
    select * from Prerequisites;
    return rc;
   end;

function getlogs
	return ref_cursor is
	rc ref_cursor;
begin
	open rc for
	select * from logs;
	return rc;
end;




procedure stud_courses(rc out sys_refcursor, bno in Students.B#%type, studcount out number)
is

begin
select count(B#) into studcount from Students where B#=bno;

open rc for
select C.classid, C.dept_code, C.course#, C.sect#, C.year, C.semester, temp.templgrade, temp.tempngrade
from Classes C, (select E.classid as tempclassid, E.lgrade as templgrade, G.ngrade as tempngrade from Enrollments E, Grades G
			where E.B# = bno and E.lgrade = G.lgrade) temp
where C.classid = temp.tempclassid;



end;


function prereq_courses(depcode in Prerequisites.dept_code%type, cno in Prerequisites.course# %type)
return ref_cursor
is
rc ref_cursor;
begin
open rc for
select (pre_dept_code||pre_course#) Course
from prerequisites
start with dept_code = depcode and course# = cno
connect by prior pre_dept_code = dept_code and prior pre_course# = course#;
return rc;
end;

 


function classes_stud(c_cid in classes.classid%type, classcount out number)
return ref_cursor is
rc ref_cursor;
begin
select count(classid) into classcount from classes where classid=c_cid;

open rc for
select  Cl.classid, c.title, S.B# , S.firstname from
classes Cl,courses c, Students S,enrollments E where
Cl.classid=c_cid and Cl.course# =c.course# and Cl.classid = E.classid and E.B# = S.B#;
return rc;
end;







procedure enroll_stud(bno in Students.B#%type, clid in Classes.classid%type, studcount out number, classcount out number, clsize out number, cllimit out number, inclass out number, clenrolled out number, prerqcount out number, isprerequisite out number  )
is

begin

	select count(B#) into studcount from students where B#=bno;
	select count(classid) into classcount from classes where classid=clid;
	select class_size into clsize from Classes where classid = clid;
	select limit into cllimit from Classes where classid = clid;
	select count(classid) into inclass from Enrollments where classid = clid and B#=bno;
	select count(E.classid) into clenrolled from Classes C, Enrollments E where C.classid = E.classid and E.B# = bno and (semester, year) in (select semester, year from classes where classid=clid);
	select count(B#) into prerqcount from enrollments E, classes C where E.classid= C.classid and E.B# = bno and lgrade not in ('D','I') and (dept_code, course#) = all (select pre_dept_code, pre_course# from prerequisites p, classes cl where p.dept_code = cl.dept_code and p.course#=cl.course# and cl.classid = clid);  
	select count(*) into isprerequisite from classes C, prerequisites P where C.dept_code = P.dept_code and C.course# = P.course# and C.classid = clid;
		
		if(studcount>0 and classcount>0) then
			if(clsize < cllimit) then
				if(inclass = 0) then
					if(isprerequisite = 0) then
						if(clenrolled =3) then
							dbms_output.put_line('You are overloaded.');
							insert into enrollments("B#", "CLASSID", "LGRADE") values(bno, clid, null);
						elsif (clenrolled > 4) then
							dbms_output.put_line('Students cannot be enrolled in more than four classes in the same semester.');
						else
							insert into enrollments("B#", "CLASSID", "LGRADE") values(bno, clid, null);
						end if;
					else
						if(prerqcount =0) then
							dbms_output.put_line('Prerequisite not satisfied.');
						else
							if(clenrolled =3) then
								dbms_output.put_line('You are overloaded.');
								insert into enrollments("B#", "CLASSID", "LGRADE") values(bno, clid, null);
							elsif (clenrolled > 4) then
								dbms_output.put_line('Students cannot be enrolled in more than four classes in the same semester.');
							else
								insert into enrollments("B#", "CLASSID", "LGRADE") values(bno, clid, null);
							end if;
						end if;
					end if;
				else
					dbms_output.put_line('Student already enrolled in class');
				end if;
			else
				dbms_output.put_line('Class is full');
			end if;
		else
			if ((studcount < 1) and (classcount < 1)) then
				dbms_output.put_line('The B# and classid are invalid.');
			else
				if ((studcount < 1) and (classcount > 0)) then
						dbms_output.put_line('The B# is invalid.');
				else
						dbms_output.put_line('The classid is invalid.');
				end if;
			end if;
		end if;
end enroll_stud;


procedure drop_stud_class(bno in Students.B#%type, clid in Enrollments.classid%type, studcount out number, classcount out number, clsize out number, classenrolled out number, allclassenrolled out number, prereqinfo out number)  is

begin

select count(B#) into studcount from students where B#=bno;
select count(classid) into classcount from Enrollments where classid = clid;
select class_size into clsize from classes where classid = clid;
select count(*) into classenrolled from enrollments where B#=bno and classid = clid;
select count(classid) into prereqinfo from classes c, prerequisites p where c.dept_code = p.pre_dept_code and c.course# = p.pre_course# and c.classid = clid and (p.dept_code, p.course#) in (select dept_code, course# from enrollments e, classes cl where e.classid = cl.classid and e.B# = bno);
select count(classid) into allclassenrolled from Enrollments where B#=bno;

	if(studcount>0 and classcount>0) then
		if(classenrolled>0) then
			if(prereqinfo=0) then
				if(allclassenrolled =1 and clsize =1) then
					delete from enrollments where B#=bno and classid= clid;
					dbms_output.put_line('This student is not enrolled in any classes.');
					dbms_output.put_line('The class now has no students.');
				elsif(allclassenrolled =1) then
					delete from enrollments where B#=bno and classid= clid;
					dbms_output.put_line('This student is not enrolled in any classes.');
				elsif(clsize=1) then
					delete from enrollments where B#=bno and classid= clid;
					dbms_output.put_line('The class now has no students.');
				else
					delete from enrollments where B#=bno and classid= clid;
				end if;
			else
				dbms_output.put_line('The drop is not permitted because another class uses it as a prerequisite');
			end if;
		else
			dbms_output.put_line('The student is not enrolled in the class');	
		end if;
	else		
		if ((studcount<1) and (classcount<1)) then
			dbms_output.put_line('The B# and classid are both invalid.');
		else
			if ((studcount<1) and (classcount>0)) then
				dbms_output.put_line('The B# is invalid.');
			else
				dbms_output.put_line('The classid is invalid.');
			end if;
		end if;
	end if;	

end;

procedure drop_student(bno in students.b#%type, studcount out number) is

begin
select count(B#) into studcount from students where B#=bno;

delete from students where students.b#=bno;

exception when NO_DATA_FOUND then 
dbms_output.put_line( 'The B# is invalid.');

end drop_student;
		
end;
/
show errors



drop trigger trigger_student_add;
create trigger trigger_student_add
	after insert on students
		for each row
	declare
		uname varchar2(20);
		utime date;
	begin
		select user into uname from dual;
		select localtimestamp into utime from dual;
		insert into logs (logid, who, time, table_name, operation, key_value)
		values (log_sequence.nextval, uname, utime, 'STUDENTS', 'INSERT', :new.B# );
	end;
/
drop trigger trigger_student_delete;
create trigger trigger_student_delete
	after delete on students
		for each row
	declare
		uname varchar2(20);
		utime date;
	begin
		select user into uname from dual;
		select localtimestamp into utime from dual;
		insert into logs (logid, who, time, table_name, operation, key_value)
		values (log_sequence.nextval, uname, utime, 'STUDENTS', 'DELETE', :old.B#);
	end;
/
drop trigger trigger_enrollments_add;
create trigger trigger_enrollments_add
	after insert on enrollments
		for each row
	declare
		uname varchar2(20);
		utime date;
	begin
		select user into uname from dual;
		select localtimestamp into utime from dual;
		
		update classes set class_size=class_size+1 where classid=:new.classid;
		insert into logs (logid, who, time, table_name, operation, key_value)
		values (log_sequence.nextval, uname, utime, 'ENROLLMENTS', 'INSERT', :new.B# || ',' || :new.classid);
	end;
/
drop trigger trigger_enrollments_delete;
create trigger trigger_enrollments_delete
	after delete on enrollments
		for each row
	declare
		uname varchar2(20);
		utime2 date;
	begin
		select user into uname from dual;
		select localtimestamp into utime2 from dual;
		update classes set class_size=class_size-1 where classid=:old.classid;
		insert into logs (logid, who, time, table_name, operation, key_value)
		values (log_sequence.nextval, uname, utime2, 'ENROLLMENTS', 'DELETE', :old.B# || ',' || :old.classid);
		
	end;
/

drop trigger del_enr_on_stud_del;
create trigger del_enr_on_stud_del
after delete on students
for each row
begin 
delete from enrollments where enrollments.b#=:old.b# ;
end;
/ 
set serveroutput on;
 show errors