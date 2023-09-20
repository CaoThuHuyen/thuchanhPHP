<?php

require_once('database.php');
//get IDs
$category_id = filter_input(INPUT_POST, 'category_id', FILTER_VALIDATE_INT);
//Validate inputs
if ($category_id == null || $category_id == false){

    // Add the product to the database  
    $query = 'DELETE FROM categories 
              WHERE categoryID = :category_id';
    $statement = $db->prepare($query);
    $statement->bindValue(':category_id', $category_id);
    $statement->execute();
    $statement->closeCursor();

    // Display the Category List page
    include('category_list.php');
}