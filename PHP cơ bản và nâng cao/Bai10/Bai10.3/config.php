
<?php
    $con= mysqli_connect("localhost","root","");
    mysqli_select_db($con, "tintuc");
    mysqli_query($con, "SET names 'utf8'");
    if(!$con){
    echo "Không thể kết nối đến Database!".mysqli_connect_error($con);
    }
?>