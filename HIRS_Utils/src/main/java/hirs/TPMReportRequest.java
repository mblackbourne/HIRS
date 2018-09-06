package hirs;

import static org.apache.logging.log4j.LogManager.getLogger;

import javax.xml.bind.annotation.XmlElement;

import org.apache.logging.log4j.Logger;

import hirs.data.persist.Report;
import hirs.data.persist.TPMReport;

import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

/**
 * <code>TPMReportRequest</code> is an immutable class and extends
 * <code>ReportRequest</code> to specify parameters which define the
 * composition of the TPM report to be generated by the client. For example
 * <code>TPMReportRequest</code> specifies which TPM PCRs should included in
 * the Client's TPM report using a TPM mask. <code>TPMReportRequest</code> also
 * provides the client with nonce (number used once) value to be used by TPM
 * when generating a TPM quote.
 */
public final class TPMReportRequest implements ReportRequest {

    private static final int MIN_NONCE_LEN = 20;

    private static final Logger LOGGER =
        getLogger(TPMReportRequest.class);

    @XmlElement
    private final int pcrMask;

    @XmlElement
    private final byte[] nonce;

    /**
     * Default constructor required to enable marshalling and unmarshalling
     * to/from xml.
     */
    protected TPMReportRequest() {
        pcrMask = 0;
        nonce = null;
    }

    /**
     * Constructor used to set values of TPM PCRs mask to be reported by the
     * client, and nonce value used by client's TPM to sign quote of these PCRs
     * values. The nonce value must be a high entropy value that will not
     * repeat.
     * @param nonce
     *        number used once to protect against reply attacks.
     * @param mask
     *        bit mask to identify the list of PCR that client should
     *        include in TPM report.
     */
    public TPMReportRequest(final byte[] nonce, final int mask) {
        LOGGER.debug("Entering constructor");

        if (nonce == null) {
            String msg = "Cannot init TPMReportRequest using null Nonce";
            LOGGER.error(msg);
            throw new NullPointerException(msg);
        }

        if (nonce.length < MIN_NONCE_LEN) {
            String msg = "Cannot init TPMReportRequest using Nonce value less"
                    + "than " + Integer.toString(MIN_NONCE_LEN)
                    + " octets in length";
            LOGGER.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if (mask == 0) {
            String msg = "Cannot init TPMReportRequest with all zero mask.";
            LOGGER.error(msg);
            throw new IllegalArgumentException(msg);
        }

        LOGGER.debug("creating new TPMReportRequest instance with Nonce = {} "
            + "0x{}", Hex.encodeHexString(nonce), Integer.toHexString(mask));

        this.nonce = Arrays.copyOf(nonce, nonce.length);
        pcrMask = mask;

        LOGGER.debug("Exiting constructor");
    }

    /* (non-Javadoc)
     * @see hirs.ReportRequest#getReportType()
     */
    @Override
    public Class<? extends Report> getReportType() {
        return TPMReport.class;
     }

    /**
     * Method returns PCR Mask for TPM PCRs that
     * a device should report in TPM report.
     * @return int
     *        mask to identify which PCRs should be included in TPM.
     * report.
     */
     public int getPcrMask() {
        return pcrMask;
     }

    /**
     * Returns nonce to be used by device TPM to generate
     * TPM quote.
     * @return byte[]
     *         value of nonce generated by appraiser.
     */
     public byte[] getNonce() {
         return nonce.clone();
     }
}
