<?php
    session_start();
    if(!isset($_SESSION['count'])){
        $_SESSION['count'] = 1;
    }else{
        $_SESSION['count']++;
    }
?>
<?php
    echo $_SESSION['count'];
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=a, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <a href="tang.php">Next</a>
</body>
</html>