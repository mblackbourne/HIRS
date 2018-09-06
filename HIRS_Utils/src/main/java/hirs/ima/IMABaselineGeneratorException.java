package hirs.ima;

/**
 * This class represents an <code>Exception</code> generated by a
 * <code>CreateIMABaseline</code>.
 */
public class IMABaselineGeneratorException extends Exception {

    private static final long serialVersionUID = 1704308568386321875L;

    /**
     * Creates a new <code>CreateIMABaselineException</code> that has the
     * message <code>msg</code>.
     *
     * @param msg
     *            exception message
     */
    IMABaselineGeneratorException(final String msg) {
        super(msg);
    }

    /**
     * Creates a new <code>CreateIMABaselineException</code> that wraps the
     * given <code>Throwable</code>.
     *
     * @param t
     *            root cause
     */
    IMABaselineGeneratorException(final Throwable t) {
        super(t);
    }

    /**
     * Creates a new <code>CreateIMABaselineException</code> that has the
     * message <code>msg</code> and wraps the root cause.
     *
     * @param msg
     *            exception message
     * @param t
     *            root cause
     */
    IMABaselineGeneratorException(final String msg, final Throwable t) {
        super(msg, t);
    }

}
