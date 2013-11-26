package org.lightadmin.core.web.util;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static javax.imageio.ImageIO.read;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.imgscalr.Scalr.Method.SPEED;
import static org.imgscalr.Scalr.Mode.AUTOMATIC;
import static org.imgscalr.Scalr.OP_ANTIALIAS;
import static org.imgscalr.Scalr.resize;
import static org.lightadmin.core.util.ResponseUtils.responseHeader;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.parseMediaType;

@SuppressWarnings("unused")
public class ImageResourceControllerSupport {

    public ResponseEntity<?> downloadImageResource(byte[] content, int width, int height) {
        if (isEmpty(content)) {
            return noContentResponse();
        }

        try {
            return writeImageResourceResponse(content, mediaTypeOf(content), width, height);
        } catch (Exception ex) {
            return serverErrorResponse();
        }
    }

    private ResponseEntity<?> writeImageResourceResponse(byte[] content, MediaType mediaType, int width, int height) throws IOException {
        if (imageResizingRequired(width, height)) {
            try {
                return scaledImageResourceResponse(content, width, height, mediaType);
            } catch (Exception ex) {
            }
        }
        return imageResourceResponse(content, mediaType);
    }

    private ResponseEntity<?> scaledImageResourceResponse(byte[] bytes, int width, int height, MediaType mediaType) throws IOException {
        BufferedImage sourceImage = read(new ByteArrayInputStream(bytes));
        BufferedImage image = resizeImage(sourceImage, width, height);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ImageIO.write(image, mediaType.getSubtype(), byteArrayOutputStream);

        return imageResourceResponse(byteArrayOutputStream.toByteArray(), mediaType);
    }

    private ResponseEntity<?> imageResourceResponse(byte[] content, MediaType mediaType) {
        return new ResponseEntity<byte[]>(content, responseHeader(content, mediaType), OK);
    }

    private ResponseEntity serverErrorResponse() {
        return new ResponseEntity(INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity noContentResponse() {
        return new ResponseEntity(NO_CONTENT);
    }

    private MediaType mediaTypeOf(final byte[] bytes) {
        ContentHandlerDecorator contentHandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        Parser parser = new AutoDetectParser();
        try {
            final ParseContext parseContext = new ParseContext();
            parser.parse(new ByteArrayInputStream(bytes), contentHandler, metadata, parseContext);
            return parseMediaType(metadata.get("Content-Type"));
        } catch (Exception e) {
            return APPLICATION_OCTET_STREAM;
        }
    }

    protected boolean imageResizingRequired(final int width, final int height) {
        return width > 0 || height > 0;
    }

    protected BufferedImage resizeImage(BufferedImage sourceImage, int width, int height) {
        final int currentWidth = sourceImage.getWidth();
        final int currentHeight = sourceImage.getHeight();

        float ratio = ((float) currentHeight / (float) currentWidth);

        if (width <= 0) {
            width = (int) (height / ratio);
        }

        if (height <= 0) {
            height = (int) (width * ratio);
        }

        return resize(sourceImage, SPEED, AUTOMATIC, width, height, OP_ANTIALIAS);
    }
}