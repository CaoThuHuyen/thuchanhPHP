<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link href="css.css" type="text/css" rel="stylesheet">
</head>
<body>
    <div id="container">
        <header>
            <h1><a href="index.php">COMPUTER ABC</a></h1>
        </header>
            <div id="main-wrapper">
                <div id="product-info"> 
                    <?php echo $html ?>
                </div>
                <div id="rating">
                    <form action="" method="POST">
                        <h3>Đánh giá</h3>
                        <input type="radio" name="rate" value="5" checked> 5
                        <input type="radio" name="rate" value="4"> 4
                        <input type="radio" name="rate" value="3"> 3
                        <input type="radio" name="rate" value="2"> 2
                        <input type="radio" name="rate" value="1"> 1<br />
                        <input type="submit" name="rate_submit" value="Rate" id="submit-button">
                    </form>
                </div>
            </div>
        <footer>
        </footer>
    </div>
</body>
</html>