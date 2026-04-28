package patterns;

import model.users.*;

public class UserFactory {

    public static User createUser(String type,
                                  String id,
                                  String login,
                                  String password,
                                  String firstName,
                                  String lastName,
                                  String email) {

        switch (type.toLowerCase()) {

            case "admin":
                return new Admin(id, login, password, firstName, lastName, email, "A001", 0);

            case "manager":
                return new Manager(id, login, password, firstName, lastName, email, "M001", 0, null);

            case "techsupport":
                return new TechSupportSpecialist(id, login, password, firstName, lastName, email, "T001", 0);

            case "employee":
                // если нужно абстрактный тип — можно не использовать
                throw new IllegalArgumentException("Cannot create abstract Employee");

            default:
                throw new IllegalArgumentException("Unknown user type: " + type);
        }
    }
}