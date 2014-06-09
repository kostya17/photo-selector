## photo-selector


 Photo-selector is solution to implement multiple photo selrction from gallery


**How to use:**

1. Start GalleryActivity from your activity like this:
```
Intent pickImage = new Intent(this, GalleryActivity.class);
startActivityForResult(pickImage, PICK_IMAGES_REQUEST);
```
2. Override `onActivityResult(int requestCode, int resultCode, Intent data)` method in your activity
and get list of images like this 
`mImages.addAll((ArrayList<Image>) data.getSerializableExtra("selected"));`


#### In `sample` folder you can find `SimpleAdapter` and `MainActivity` to see how it works.
