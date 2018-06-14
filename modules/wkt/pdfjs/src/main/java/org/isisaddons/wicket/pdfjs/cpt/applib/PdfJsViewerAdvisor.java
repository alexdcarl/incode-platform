package org.isisaddons.wicket.pdfjs.cpt.applib;

import java.io.Serializable;

import org.wicketstuff.pdfjs.Scale;

import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.bookmark.Bookmark;
import org.apache.isis.applib.value.Blob;

/**
 * SPI service interface.
 */
public interface PdfJsViewerAdvisor {

    @Programmatic
    Advice advise(final InstanceKey instanceKey);
    @Programmatic
    void pageNumChangedTo(final InstanceKey instanceKey, final int pageNum);
    @Programmatic
    void scaleChangedTo(final InstanceKey instanceKey, final Scale scale);
    @Programmatic
    void heightChangedTo(final InstanceKey instanceKey, final int height);

    /**
     * Key for the rendering of a specific property of an object for an named individual.
     *
     * <p>
     *     This is a (serializable) value type so that, for example, implementations can use as a key within a hash structure.
     * </p>
     */
    class InstanceKey implements Serializable {

        private final TypeKey typeKey;
        private final String identifier;

        public InstanceKey(
                final String objectType,
                final String identifier,
                final String propertyId,
                final String userName) {
            this.typeKey = new TypeKey(objectType, propertyId, userName);
            this.identifier = identifier;
        }

        @Programmatic
        public TypeKey getTypeKey() {
            return typeKey;
        }

        /**
         * The identifier of the object being rendered.
         *
         * <p>
         *     The {@link TypeKey#getObjectType()} and {@link #getIdentifier()} together constitute the object's
         *     identity (in effect, its {@link Bookmark}).
         * </p>
         */
        @Programmatic
        public String getIdentifier() {
            return identifier;
        }

        @Programmatic
        public Bookmark asBookmark() {
            return new Bookmark(typeKey.objectType, identifier);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            final InstanceKey instanceKey = (InstanceKey) o;

            if (typeKey != null ? !typeKey.equals(instanceKey.typeKey) : instanceKey.typeKey != null)
                return false;
            return identifier != null ? identifier.equals(instanceKey.identifier) : instanceKey.identifier == null;
        }

        @Override
        public int hashCode() {
            int result = typeKey != null ? typeKey.hashCode() : 0;
            result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ViewerKey{" +
                    "typeKey=" + typeKey +
                    ", identifier='" + identifier + '\'' +
                    '}';
        }

        /**
         * Key for the rendering of a specific property of an object's type for an named individual.
         *
         * <p>
         *     This is a (serializable) value type so that, for example, implementations can use as a key within a hash structure.
         * </p>
         */
        public static class TypeKey implements Serializable {

            private final String objectType;
            private final String propertyId;
            private final String userName;

            public TypeKey(
                    final String objectType,
                    final String propertyId,
                    final String userName) {
                this.objectType = objectType;
                this.propertyId = propertyId;
                this.userName = userName;
            }

            /**
             * The object type of the object being rendered.
             */
            @Programmatic
            public String getObjectType() {
                return objectType;
            }

            /**
             * The property of the object (a {@link Blob} containing a PDF) being rendered.
             */
            @Programmatic
            public String getPropertyId() {
                return propertyId;
            }

            /**
             * The user for whom the object's property is being rendered.
             */
            @Programmatic
            public String getUserName() {
                return userName;
            }

            @Override
            public boolean equals(final Object o) {
                if (this == o)
                    return true;
                if (o == null || getClass() != o.getClass())
                    return false;

                final TypeKey typeKey = (TypeKey) o;

                if (objectType != null ? !objectType.equals(typeKey.objectType) : typeKey.objectType != null)
                    return false;
                if (propertyId != null ? !propertyId.equals(typeKey.propertyId) : typeKey.propertyId != null)
                    return false;
                return userName != null ? userName.equals(typeKey.userName) : typeKey.userName == null;
            }

            @Override public int hashCode() {
                int result = objectType != null ? objectType.hashCode() : 0;
                result = 31 * result + (propertyId != null ? propertyId.hashCode() : 0);
                result = 31 * result + (userName != null ? userName.hashCode() : 0);
                return result;
            }

            @Override
            public String toString() {
                return "TypeKey{" +
                        "objectType='" + objectType + '\'' +
                        ", propertyId='" + propertyId + '\'' +
                        ", userName='" + userName + '\'' +
                        '}';
            }
        }
    }

    /**
     * Immutable value type.
     *
     * <p>
     *     The <code>withXxx</code> allow clones of the value to be created, for convenience of implementors.
     * </p>
     */
    class Advice implements Serializable {

        private final Integer pageNum;
        private final TypeAdvice typeAdvice;

        public Advice(final Integer pageNum, final TypeAdvice typeAdvice) {
            this.pageNum = pageNum;
            this.typeAdvice = typeAdvice;
        }

        @Programmatic
        public Integer getPageNum() {
            return pageNum;
        }

        @Programmatic
        public Scale getScale() {
            return typeAdvice.getScale();
        }

        @Programmatic
        public Integer getHeight() {
            return typeAdvice.getHeight();
        }

        @Programmatic
        public Advice withPageNum(Integer pageNum) {
            return new Advice(pageNum, this.typeAdvice);
        }

        @Programmatic
        public Advice withTypeAdvice(TypeAdvice typeAdvice) {
            return new Advice(this.pageNum, typeAdvice);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            final Advice advice = (Advice) o;

            if (pageNum != null ? !pageNum.equals(advice.pageNum) : advice.pageNum != null)
                return false;
            return typeAdvice != null ? typeAdvice.equals(advice.typeAdvice) : advice.typeAdvice == null;
        }

        @Override
        public int hashCode() {
            int result = pageNum != null ? pageNum.hashCode() : 0;
            result = 31 * result + (typeAdvice != null ? typeAdvice.hashCode() : 0);
            return result;
        }

        /**
         * Immutable value type representing the scale/height to render a PDF.
         *
         * <p>
         *     The <code>withXxx</code> allow clones of the value to be created, for convenience of implementors.
         * </p>
         */
        public static class TypeAdvice implements Serializable {

            private final Scale scale;
            private final Integer height;

            public TypeAdvice(final Scale scale, final Integer height) {
                this.scale = scale;
                this.height = height;
            }

            @Programmatic
            public Scale getScale() {
                return scale;
            }

            @Programmatic
            public Integer getHeight() {
                return height;
            }

            @Programmatic
            public TypeAdvice withScale(Scale scale) {
                return new TypeAdvice(scale, this.height);
            }

            @Programmatic
            public TypeAdvice withHeight(Integer height) {
                return new TypeAdvice(this.scale, height);
            }

            @Override
            public boolean equals(final Object o) {
                if (this == o)
                    return true;
                if (o == null || getClass() != o.getClass())
                    return false;

                final TypeAdvice that = (TypeAdvice) o;

                if (scale != that.scale)
                    return false;
                return height != null ? height.equals(that.height) : that.height == null;
            }

            @Override
            public int hashCode() {
                int result = scale != null ? scale.hashCode() : 0;
                result = 31 * result + (height != null ? height.hashCode() : 0);
                return result;
            }

            @Override
            public String toString() {
                return "TypeAdvice{" +
                        "scale=" + scale +
                        ", height=" + height +
                        '}';
            }
        }
    }

}
