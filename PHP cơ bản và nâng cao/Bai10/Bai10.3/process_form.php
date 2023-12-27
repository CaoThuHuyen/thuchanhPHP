<?php
    include 'config.php';
?>
<?php
     session_start();
     if($_SERVER['REQUEST_METHOD'] = 'POST' && isset($_POST['login_btn'])){
         $name = $_POST['username'];
         $pass = $_POST['password'];
         $sel = "SELECT * FROM users WHERE Username = '$name' AND Password = '$pass'";
         $result = mysqli_query($con, $sel);
         $row = mysqli_fetch_assoc($result);
         $kt = mysqli_num_rows($result);
 
         if($kt > 0){
             $_SESSION['username'] = $name;
             $_SESSION['password'] = $pass;
             if(isset($_POST['remember']) and ($_POST['remember']=="on")){
                 setcookie("username", $name, time() + 60);
                 setcookie("password", $pass, time() + 60);
             }
             echo "<script>alert('Login Successfull.');
             location.href='trangquantriadmin.php';</script>";
             echo "Chào bạn: ".$row['name']."<br>Pass của bạn là: ".$row['pass'];
         }else{
             echo "<script>alert('Login Failed.');</script>";
         }
     }
?>