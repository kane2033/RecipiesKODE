package com.kode.recipes.presentation.recipe


/*
class SwipeableImagesAdapter: PagerAdapter() {
    // Context object
    var context: Context? = null

    // Array of images
    lateinit var images: IntArray

    // Layout Inflater
    var mLayoutInflater: LayoutInflater? = null


    // Viewpager Constructor
    fun ViewPagerAdapter(context: Context, images: IntArray) {
        this.context = context
        this.images = images
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
    }

    override fun getCount(): Int {
        // return the number of images
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // inflating the item.xml
        val itemView: View =
            mLayoutInflater?.inflate(R.layout.item_image_swipable, container, false) ?:

        // referencing the image view from the item.xml file
        val imageView: ImageView = itemView.findViewById<View>(R.id.imageView) as ImageView

        // setting the image in the imageView
        imageView.setImageResource(images[position])

        // Adding the View
        container.addView(itemView)
        return itemView
    }
}*/
