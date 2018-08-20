import java.sql.{Connection,DriverManager}

object database extends App{
  val url = "jdbc:mysql://127.0.0.1:3306/db"
  val driver = "com.mysql.jdbc.Driver"
  val username = "root"
  val password = "12345"
  var connection:Connection = _

  println("\nThe Table Details\n")
  ViewTable()
  def ViewTable() : Unit={
    try {
      Class.forName(driver)
      connection= DriverManager.getConnection(url,username,password)
      val Statement=connection.createStatement
      val rs = Statement.executeQuery("select * from EmpDetails")
      while (rs.next){
        val DeptID= rs.getString("DeptID")
        val EmpName = rs.getString("EmpName")
        val Salary = rs.getString("Salary")
        println("Deptid = %s\t Name = %s\t Salary = %s\n".format(DeptID,EmpName,Salary))
      }
    }catch {
      case e:  Exception => e.printStackTrace()
    }
  }

  MaxSalGroup()

  println("Do you want increase the salary of employees by 15%? (yes/no)")
  val ch=readLine();
  if(ch.equalsIgnoreCase("yes")){
    IncSal()
  }

  connection close()

  def MaxSalGroup() : Unit={
    try{
      println("\nMaximum earning employee in each department\n")
      val rs = connection.createStatement.executeQuery("SELECT DeptID, EmpName, MAX(Salary) FROM EmpDetails GROUP BY DeptID")
      while(rs.next){
        val DeptId=rs.getString("DeptID")
        val name=rs.getString("EmpName")
        val Salary = rs.getString("MAX(Salary)")
        println("Deptid = %s\t Name = %s\t Salary = %s\n".format(DeptId,name,Salary))
      }
    }catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def IncSal() : Unit={
    try{
      println("\n After increment of 15% of salary\n")
      val rs=connection.createStatement.executeQuery("select DeptID,EmpName,Salary+(Salary*15/100) as NewSalary from EmpDetails")
      while (rs.next){
        val DeptId=rs.getString("DeptID")
        val name=rs.getString("EmpName")
        val Salary = rs.getString("NewSalary")
        println("Deptid = %s\t Name = %s\t Salary = %s\n".format(DeptId,name,Salary))
      }
      println("Want to save these data? (yes/no) ")
      val ch=readLine()
      if(ch.equalsIgnoreCase("yes")){
          UpdateSal()
      }
    }catch {
      case e: Exception =>e.printStackTrace()
    }
  }

  def UpdateSal() : Unit={
    try{
      println("\nUpdated table\n ")
      val rs=connection.createStatement.executeUpdate("update EmpDetails set Salary=Salary+(Salary*15/100)")
      ViewTable()
    }catch {
      case e: Exception=>e.printStackTrace()
    }
  }
}
