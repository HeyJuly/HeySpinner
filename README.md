# HeySpinner

An custom spinner 

## Usage
```
implementation 'com.hey.lib:HeySpinner:1.0.0'
```

Add the tag into the XML layout:

```

<com.hey.lib.HeySpinner
    android:id="@+id/spinner"
    android:layout_width="match_parent"
    android:layout_height="45dp"
    android:clickable="true"
    android:enabled="true"
    android:gravity="center_vertical"
    android:paddingLeft="10dp"
    android:text="item1" />
        
```


```
List<String> data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        spinner = findViewById(R.id.spinner);
        spinner.attachData(data);
```


## Attributes

name | type | info 
-|-|-|
itemTextColor | color |  dropdown item textcolor
showArrow  | boolean| show the arrow 
arrowSrc    |   reference |set the drawable of the drop-down arrow



## Custom 

```
class CustomAdapter extends HeySpinnerBaseAdapter<String> {

    private List<String> data;
    private Context context;
    
    public CustomAdapter( Context context, List<String> data, int textColor, int textGravity) {
        super(textColor, textGravity);
        this.data = data;
        this.context=context;
    }

    @Override
    protected int getViewId() {
        return R.id.textView;
    }

    @Override
    protected View bindView(int position, View convertView, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_spinner,null);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

spinner.setAdapter(new CustomAdapter(this,data,Color.BLACK,Gravity.LEFT));

```


Thanks for NiceSpinner https://github.com/arcadefire/nice-spinner
