<?php
    include 'config.php';
    session_start();
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    TRANG QUAN TRI
    <?php
        if(isset($_COOKIE['username']) and isset($_COOKIE['password'])){
            echo "Xin chào: ".$_COOKIE['username'];
            echo "<br><a href='logout.php'>Logout</a>";
        }
        else{
            if(isset($_SESSION['username']) and isset($_SESSION['password'])){
            echo "Xin chào: ".$_SESSION['username'];
            echo "<br><a href='logout.php'>Logout</a>";
            }
            else{
                echo "<script>alert('Bạn chưa đăng nhập');
                location.href='login.php';</script>";
            }
        }
        
    ?>
    <a href="logout.php" >Thoat ra </a>
</body>
</html>