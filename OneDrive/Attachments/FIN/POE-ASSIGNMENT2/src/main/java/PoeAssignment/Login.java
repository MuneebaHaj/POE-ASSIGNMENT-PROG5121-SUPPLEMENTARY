package PoeAssignment;

import static PoeAssignment.POEASSIGNMENT2.cellNumber;
import static PoeAssignment.POEASSIGNMENT2.cellRegex;


public class Login {
       // === Password Validation Helper Method ===
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        return hasUpper && hasDigit && hasSpecial;
    }
    public boolean checkUserName(){
     POEASSIGNMENT2 POE2 = new POEASSIGNMENT2();  
     boolean Validation = false;
     if(POE2.username != null && POE2.username.contains("_") && POE2.username.length() <= 5){
         Validation = true;
     }
     else
     {
     Validation = false;
     };      
      return Validation  ;
    }
  // No-argument version
public boolean checkCellPhoneNumber() {
    return POEASSIGNMENT2.cellNumber != null && POEASSIGNMENT2.cellNumber.matches(POEASSIGNMENT2.cellRegex);
}

// Argument version for testing individual numbers
public boolean checkCellPhoneNumber(String number) {
    return number != null && number.matches(POEASSIGNMENT2.cellRegex);
}
   public String registerUser() {
    return "User successfully registered.\n"
         + "Name: " + POEASSIGNMENT2.firstName + " " + POEASSIGNMENT2.lastName + "\n"
         + "Username: " + POEASSIGNMENT2.username + "\n"
         + "Password: " + POEASSIGNMENT2.password;
}
   public boolean loginUser(String loginUsername, String loginPassword) {
    return POEASSIGNMENT2.username.equals(loginUsername) &&
           POEASSIGNMENT2.password.equals(loginPassword);
       
   }
   public String returnLoginStatus(boolean loginSuccessful) {
    if (loginSuccessful) {
        return "Welcome " + POEASSIGNMENT2.firstName + " " + POEASSIGNMENT2.lastName + ", it is great to see you again.";
    } else {
        return "Username or password incorrect, please try again.";
    }
    
}
   public String checkUserNameMessage(String username) {
    if (username.contains("_") && username.length() <= 5) {
        return "Username is correctly formatted.";
    } else {
        return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
    }
}

public String checkPasswordMessage(String password) {
    if (checkPasswordComplexity(password)) {
        return "Password successfully captured.";
    } else {
        return "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    }
}

public String checkCellPhoneMessage(String cellNumber) {
    if (cellNumber != null && cellNumber.matches(POEASSIGNMENT2.cellRegex)) {
        return "Cell number successfully captured.";
    } else {
        return "Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
    }
}
}
