package com.example.mypc.officaligif.networks;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MediaResponse {
    public List<DataJSON> data;
    public PaginationJSON pagination;
    public MetaJSON meta;

    public class DataJSON {
        public ImagesJSON images;
        public String title;
        public String id;
        public String source_tld;
        public String caption;


        public class ImagesJSON{
            public OriginalJSON original;
            public PreviewGifJSON preview_gif;
            public FixedHeightJSON fixed_height;
            public FixedWidthJSON fixed_width;
            public FixedHeightSmallJSON fixed_height_small;
            public FixedWidthSmallJSON fixed_width_small;
            public FixedWidthStillJSON fixed_width_still;
            public FixedWidthDownsampledJSON fixed_width_downsampled;
            public FixedHeightDownsampledJSON fixed_height_downsampled;
            public OriginalMp4JSON original_mp4;

            public class FixedHeightJSON{
                public String url;
                public String width;
                public String height;
                public String size;

            }
            public class FixedWidthJSON{
                public String url;
                public String width;
                public String height;
                public String size;

            }

            public class FixedWidthSmallJSON{
                public String url;
                public String width;
                public String height;
                public String size;
            }
            public class FixedHeightSmallJSON{
                public String url;
                public String width;
                public String height;
                public String size;
            }
            public class FixedWidthStillJSON{
                public String url;
                public String width;
                public String height;
                public String size;
            }



            public class PreviewGifJSON{
                public String url;
                public String width;
                public String height;
                public String size;
            }
            public class OriginalJSON{
                public String url;
                public String width;
                public String height;
                public String size;


            }

            public class FixedWidthDownsampledJSON{
                public String url;
                public String width;
                public String height;
                public String size;
            }
            public class FixedHeightDownsampledJSON{
                public String url;
                public String width;
                public String height;
                public String size;
            }

            public class OriginalMp4JSON{
                public String mp4;
            }
        }
    }

    public class PaginationJSON {
        public int count;
        public int total_count;
    }

    public class MetaJSON {
        public String status;
    }
}
