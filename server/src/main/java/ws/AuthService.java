package ws;

import org.eclipse.jetty.server.Authentication;

public interface AuthService {
    Authentication.User validate(String authToken);
}
