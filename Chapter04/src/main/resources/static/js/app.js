Ext.onReady(function() {
    Ext.create("Ext.form.Panel", {
        title : "Controls",
        url: "/post",
        baseParams: {id: '123', foo: 'bar'},
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
                  click : function(src) {
                      src.up("form").submit({
                          success : function (form, action) {
                              // should look at result
                              console.dir(action);
                              Ext.Msg.alert('Success', "Successfully submitted");
                          },
                          failure:  function (form, action) {
                              console.dir(action);
                              switch(action.failureType) {
                                  case Ext.form.action.Action.CLIENT_INVALID:
                                      console.log("client-side validation failed");
                                      break;

                                  case Ext.form.action.Action.CONNECT_FAILURE:
                                      console.log("connection failed");
                                      Ext.Msg.alert('Failure', 'Ajax communication failed');
                                      break;

//                                  case Ext.form.action.Action.LOAD_FAILURE:
//                                      console.log("load failure - no response");
//                                      Ext.Msg.alert('Failure', 'success : true but no content');
//                                      break;
//
                                  case Ext.form.action.Action.SERVER_INVALID:
                                      console.log("server rejected");
                                      Ext.Msg.alert('Failure', action.result.message);
                                      break;
                              }
                          }
                      });
                  }
              }
          }
        ],
        renderTo : Ext.getBody()
    });
});