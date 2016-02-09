package org.nuvola.mobile.prixpascher.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Products implements Parcelable {
	public static final String TAG_ID = "id";
	public static final String TAG_TITLE = "title";
    public static final String TAG_LINK = "link";
    public static final String TAG_PRICE = "price";
    public static final String TAG_IMAGE = "image";
    public static final String TAG_CATEGORY = "category";
    public static final String TAG_SUB_CATEGORY = "subCategory";
    public static final String TAG_PRODUCT_CATEGORY = "productCategory";
    public static final String TAG_SHOP_NAME = "shopName";
    public static final String TAG_SHOP_TYPE = "shopType";
    public static final String TAG_VIEWS= "views";
    public static final String TAG_DATE_VIEWED = "viewed";
    public static final String TAG_DATE_TRACKING = "trackingDate";
	public static final String TAG_PROMOTED = "promoted";


	String id;
	String title;
    String link;
    String price;
    String image;
    String category;
    String subCategory;
    String productCategory;
    String shopName;
    String shopType;
    int views;
    String viewed;
    String trackingDate;
    String promoted;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getTrackingDate() {
        return trackingDate;
    }

    public void setTrackingDate(String trackingDate) {
        this.trackingDate = trackingDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getViewed() {
        return viewed;
    }

    public void setViewed(String viewed) {
        this.viewed = viewed;
    }

    public String getPromoted() {
        return promoted;
    }

    public void setPromoted(String promoted) {
        this.promoted = promoted;
    }

    public Products() {
		// TODO Auto-generated constructor stub
	}

	public Products(Parcel in) {
		id = in.readString();
		title = in.readString();
        link = in.readString();
        price = in.readString();
        image = in.readString();
        category = in.readString();
        subCategory = in.readString();
        productCategory = in.readString();
        shopName = in.readString();
        shopType = in.readString();
        views = in.readInt();
		viewed = in.readString();
		trackingDate = in.readString();
		promoted = in.readString();
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(title);
        dest.writeString(link);
        dest.writeString(price);
        dest.writeString(image);
        dest.writeString(category);
        dest.writeString(subCategory);
        dest.writeString(productCategory);
        dest.writeString(shopName);
        dest.writeString(shopType);
        dest.writeInt(views);
        dest.writeString(viewed);
        dest.writeString(trackingDate);
		dest.writeString(promoted);
	}

	public static final Parcelable.Creator<Products> CREATOR = new Parcelable.Creator<Products>() {
		@Override
		public Products createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new Products(in);
		}

		@Override
		public Products[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Products[size];
		}
	};
}
