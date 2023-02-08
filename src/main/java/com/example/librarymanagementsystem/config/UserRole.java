package com.example.librarymanagementsystem.config;

public enum UserRole {

  /**
   * User can do anything to manage library by (show, add, edit, delete books)
   */
  ADMIN,
  /**
   * User can (add/edit) books but not delete them
   */
  PUBLISHER,
  /**
   * User can only show books and not able to edit anything
   */
  READ_ONLY
}

