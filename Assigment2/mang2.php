<!DOCTYPE html>
<html>
<head>
<title>PHÁT SINH MẢNG VÀ TÍNH TOÁN</title>
<meta charset="utf-8">
<style>
*{
 font-family: Tahoma;
}
table{
 width: 400px;
 margin: 100px auto;
}
table th{
 background: #66CCFF;
 padding: 10px;
 font-size: 18px;
}
</style>
</head>
<body>
<form action="mang2.php" method="POST">
<table>
<thead>
<tr>
<th colspan="2">PHÁT SINH MẢNG VÀ TÍNH TOÁN</th>
</tr>
</thead>
<tbody>
<tr>
<td>Nhập số phần tử:</td>
<td><input type="text" name="so_phan_tu" width="100%"></td>
</tr>

<?php
$mang_so = array();
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    if (isset($_POST['so_phan_tu'])) {
        $n = $_POST["so_phan_tu"];
        for ($i = 0; $i < $n; $i++) {
            $mang_so[$i] = mt_rand(0, 20);
        }
    }
}
?>
<tr>
<td></td>
<td><input type="submit" value="Phát sinh và tính toán"></td>
</tr>

<tr>
<td>Mảng: </td>
<td><input type="text" name="mang_so" disabled="disabled" value="<?php echo implode(" ", $mang_so); ?>"></td>
</tr>

<tr>
<td>GTLN ( MAX ) trong mảng: </td>
<td><input type="text" name="gtln" disabled="disabled" value="<?php echo max($mang_so); ?>"></td>
</tr>

<tr>
<td>GTNN ( MIN ) trong mảng: </td>
<td><input type="text" name="ttnn" disabled="disabled" value="<?php echo min($mang_so); ?>"></td>
</tr>

<tr>
<td>Tổng mảng: </td>
<td><input type="text" name="tong" disabled="disabled" value="<?php echo array_sum($mang_so); ?>"></td>
</tr>
</tbody>
</table>
</form>
</body>
</html>