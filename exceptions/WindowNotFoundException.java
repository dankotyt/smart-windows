package exceptions;

public class WindowNotFoundException extends RuntimeException {
  public WindowNotFoundException(String message) {
    super(message);
  }
}
