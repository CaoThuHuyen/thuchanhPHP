<?php
include_once('../connect.php');

// Kiểm tra xem có dữ liệu được gửi lên không
if (isset($_POST['TenTL']) && isset($_POST['ThuTu']) && isset($_POST['AnHien']) && isset($_FILES['image'])) {
    // Lấy thông tin về hình ảnh
    $icon = $_FILES['image']['name'];
    $anhminhhoa_tmp = $_FILES['image']['tmp_name'];
    move_uploaded_file($anhminhhoa_tmp, "../image/" . $icon);

    // Lấy dữ liệu từ form
    $theloai = $_POST['TenTL'];
    $thutu = $_POST['ThuTu'];
    $an = $_POST['AnHien'];

    // Tạo câu truy vấn SQL
    $sql = "INSERT INTO theloai (TenTL, ThuTu, AnHien, icon) VALUES ('$theloai', '$thutu', '$an', '$icon')";

    // Thực hiện truy vấn
    if (mysqli_query($connect, $sql)) {
        echo "<script language='javascript'>alert('Thêm thành công');";
        echo "location.href='theloai.php';</script>";
    } else {
        echo 'Lỗi: ' . mysqli_error($connect);
    }
} else {
    echo 'Không có dữ liệu gửi lên.';
}

// Đóng kết nối
mysqli_close($connect);
?>