package hirs.data.persist.certificate;

import hirs.persist.CertificateManager;
import hirs.persist.CertificateSelector;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * This class persists Certificate Authority credentials by extending the base Certificate
 * class with fields unique to CA credentials.
 */
@Entity
public class CertificateAuthorityCredential extends Certificate {
    @SuppressWarnings("PMD.AvoidUsingHardCodedIP") // this is not an IP address; PMD thinks it is
    private static final String SUBJECT_KEY_IDENTIFIER_EXTENSION = "2.5.29.14";

    /**
     * Holds the name of the 'subjectKeyIdentifier' field.
     */
    public static final String SUBJECT_KEY_IDENTIFIER_FIELD = "subjectKeyIdentifier";

    @Column
    private final byte[] subjectKeyIdentifier;

    /**
     * This class enables the retrieval of CertificateAuthorityCredentials by their attributes.
     */
    public static class Selector extends CertificateSelector<CertificateAuthorityCredential> {
        /**
         * Construct a new CertificateSelector that will use the given {@link CertificateManager} to
         * retrieve one or many CertificateAuthorityCredentials.
         *
         * @param certificateManager the certificate manager to be used to retrieve certificates
         */
        public Selector(final CertificateManager certificateManager) {
            super(certificateManager, CertificateAuthorityCredential.class);
        }

        /**
         * Specify a subject key identifier that certificates must have to be considered
         * as matching.
         *
         * @param subjectKeyIdentifier a subject key identifier buffer to query, not empty or null
         * @return this instance (for chaining further calls)
         */
        public Selector bySubjectKeyIdentifier(final byte[] subjectKeyIdentifier) {
            setFieldValue(SUBJECT_KEY_IDENTIFIER_FIELD, subjectKeyIdentifier);
            return this;
        }
    }

    /**
     * Get a Selector for use in retrieving CertificateAuthorityCredentials.
     *
     * @param certMan the CertificateManager to be used to retrieve persisted certificates
     * @return a CertificateAuthorityCredential.Selector instance to use for retrieving certificates
     */
    public static Selector select(final CertificateManager certMan) {
        return new Selector(certMan);
    }

    /**
     * Construct a new CertificateAuthorityCredential given its binary contents.  The given
     * certificate should represent either an X509 certificate or X509 attribute certificate.
     *
     * @param certificateBytes the contents of a certificate file
     * @throws IOException if there is a problem extracting information from the certificate
     */
    public CertificateAuthorityCredential(final byte[] certificateBytes)
            throws IOException {
        super(certificateBytes);
        this.subjectKeyIdentifier =
                getX509Certificate().getExtensionValue(SUBJECT_KEY_IDENTIFIER_EXTENSION);
    }

    /**
     * Construct a new CertificateAuthorityCredential by parsing the file at the given path.
     * The given certificate should represent either an X509 certificate or X509 attribute
     * certificate.
     *
     * @param certificatePath the path on disk to a certificate
     * @throws IOException if there is a problem reading the file
     */
    public CertificateAuthorityCredential(final Path certificatePath)
            throws IOException {
        super(certificatePath);
        this.subjectKeyIdentifier =
                getX509Certificate().getExtensionValue(SUBJECT_KEY_IDENTIFIER_EXTENSION);
    }

    /**
     * Default constructor for Hibernate.
     */
    protected CertificateAuthorityCredential() {
        subjectKeyIdentifier = null;
    }

    /**
     * @return this certificate's subject key identifier.
     */
    public byte[] getSubjectKeyIdentifier() {
        if (null != subjectKeyIdentifier) {
            return subjectKeyIdentifier.clone();
        }
        return null;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        CertificateAuthorityCredential that = (CertificateAuthorityCredential) o;

        return Arrays.equals(subjectKeyIdentifier, that.subjectKeyIdentifier);
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(subjectKeyIdentifier);
        return result;
    }
}
