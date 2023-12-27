<?php
    session_start();
    if(!isset($_SESSION['name'])){
        $_SESSION['name'] = 'hocweb.com.vn';
        $_SESSION['age'] = '120';
    }
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>demo session</title>
</head>
<body>
    <?php
        echo "ten ban la: ".$_SESSION['name']."<br>";
        echo "Tuoi cua ban: ". $_SESSION['age']."<br>";
    ?>
    <a href="test_session.php">Click_here!</a>
</body>
</html>