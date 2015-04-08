Ext.onReady(function() {
    Ext.create("Ext.form.Panel", {
        title : "Controls",
        url: "/post",
        defaults: { 
            msgTarget: "side" 
        },
        items : [ 
          {
            xtype : "radiogroup",
            fieldLabel : "Title",
            vertical : true,
            columns : 1,
            items : [ {
                boxLabel : "Mr",
                name : "title",
                inputValue: "MR"
            }, {
                boxLabel : "Mrs",
                name : "title",
                inputValue: "MRS"
            }, {
                boxLabel : "Ms",
                name : "title",
                inputValue: "MS"
            } ]
          }, 
          { 
              xtype: "textfield", 
              fieldLabel: "Name",
              allowBlank: false,
              maxLength: 50
          },
          { 
              xtype: "datefield", 
              fieldLabel: "Date of birth",
              format: "d/m/Y"
          },
          {
              xtype: "textfield", 
              fieldLabel: "Blog",
              vtype: "url"
          },
          {
              xtype: "numberfield", 
              fieldLabel: "Years of experience",
              minValue: 1,
              maxValue: 15
          },
          {
              xtype: "textarea", 
              fieldLabel: "Address",
              validator: function(val) {
                  if (val.indexOf("#") != -1 || val.indexOf("!") != -1) {
                      return "Invalid characters like # or ! in address";
                  } else {
                      return true;
                  }
              }
          },
          {
              xtype: "button", 
              text: "Submit",
              listeners : {
                  "click" : function(src) {
                      src.up("form").submit();
                  }
              }
          }
        ],
        renderTo : Ext.getBody()
    });
});