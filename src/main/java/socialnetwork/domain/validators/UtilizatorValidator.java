package socialnetwork.domain.validators;

import socialnetwork.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        //TODO: implement method validate
        int error = 0;
        if (entity.getFirstName().length() < 3)
            error++;
        if (entity.getLastName().length() < 3)
            error++;
        if(entity.getPassword().length()<3)
            error++;
        if(entity.getId()<0)
            error++;
        if(entity.getId()==null)
            error++;
        if (error != 0)
            throw new ValidationException("FirstName,LastName and password must have at least 3 letters and id can not be negative or null");
    }

}
