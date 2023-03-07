package socialnetwork.domain.validators;

import socialnetwork.domain.Message;

public class MessageValidator implements Validator<Message>{
    @Override
    public void validate(Message entity) throws ValidationException {
        int error=0;
        if(entity.getMessage().length()<1)
            error++;
        if(error!=0)
            throw new ValidationException("messaj invalid!");

    }
}
