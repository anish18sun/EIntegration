package com.hypervisor.eintegrator.utils;//this class contains the initial implementation of the chatbot interface

public class Chatbot {

    public ResponseObj initiateChat(){
        String response = "Initializing EIntegration ChatBot :-)\n"
                + "As of now this chatbox is capable of performing following services"+"\n"
                + "1. Get Bhamashah Details" + "\n"
                + "2. Get Bill Details and Make Payment" + "\n"
                + "3. get Entitlements" + "\n"
                + "4. Register new member of your family";
        ResponseObj obj = new ResponseObj();
        obj.setResult(true);
        obj.setResponse(response);
        return obj;
    }

//    private void intiateChatBot(String str) {
//        //the String of messages for the user
//        String userInputString = "";
//        String response = "";
//        //introduce the chatbot to the user
//        System.out.println("Initiating Eintegration chatbot...");
//        System.out.println("This chatbot is capable of performing the following services :-");
//        System.out.println("1. Get Bhamashah details.");
//        System.out.println("2. Get Bill details.");
//        System.out.println("3. Get Entitlements.");
//        System.out.println("4. Make Bill payment.");
//
//        response = (Math.random() < 0.5) ? Constants.welcomeMsgs[0] + "\n" : Constants.welcomeMsgs[1] + "\n";
//        response += (Math.random() < 0.5) ? Constants.greetMsgs[0] + "\n" : Constants.greetMsgs[1] + "\n";
//        response += (Math.random() < 0.5) ? Constants.introMsgs[0] + "\n" : Constants.introMsgs[1] + "\n";
//
//        System.out.println(response);
//        userInputString = str;    //take the first input from the user
//
//        if (!userInputString.equalsIgnoreCase("exit") && !userInputString.equalsIgnoreCase("bye")) {
//
//            response = (Math.random() < 0.5) ? Constants.ackMsgs[0] + "\n" : Constants.ackMsgs[1] + "\n";
//            System.out.println(response);
//
//            String appResult = processString(userInputString);
//            if (appResult.equalsIgnoreCase("correct response")) {
//                System.out.println("App result : " + appResult);
//                response = (Math.random() < 0.5) ? Constants.correctMsgs[0] + "\n" : Constants.correctMsgs[1] + "\n";
//            } else response = (Math.random() < 0.5) ? Constants.errorMsgs[0] + "\n" : Constants.errorMsgs[1] + "\n";
//            System.out.println(response);
//        }
//
//        response = (Math.random() < 0.5) ? Constants.byeMsgs[0] + "\n" : Constants.byeMsgs[1] + "\n";
//        System.out.println(response);
//    }
//
//    private String processString(String userInputString) {
//
//        String response = "";
//        int index = 0;
//        String inputWords[] = userInputString.split(" ");
//
//        for (String wordToken : inputWords) {
//            if (wordToken.equalsIgnoreCase("bhamashah"))
//                response = getBhamashahDetails();
//            else if (wordToken.equalsIgnoreCase("entitlements"))
//                response = getEntitlements();
//            else if (wordToken.equalsIgnoreCase("bill")) {
//                if (inputWords[index + 1].equalsIgnoreCase("payment")) {
//                    //TODO : display the payment details for the user
//                } else {
//                    //TODO : display the bill details for some bill
//                }
//            }
//            index++;
//        }
//        return null;
//    }
//
//    private String getBhamashahDetails() {
//        //TODO : return Bhamashah details for the individual user
//
//        return "";
//    }
//
//    private String getEntitlements() {
//        //TODO : return the scheme entitlement details
//
//        return "";
//    }
//
//    private String getPaymentDetails(String str){
//        String response = "";
//
//        if(str.contains(Constants.AIRTEL_LOWCASE)
//                || str.contains(Constants.AIRTEL_UPPERCASE)){
//            // initiate airtel bill
//        }else if(str.contains(Constants.IDEA_LOWCASE)
//                || str.contains(Constants.IDEA_UPPERCASE)){
//            // initiate Idea bill
//        }else if(str.contains(Constants.VODAFONE_LOWCASE)
//                || str.contains(Constants.VODAFONE_UPPERCASE)){
//            // initiate Idea bill
//        }else if((str.contains(Constants.MTS_LOWCASE) || str.contains(Constants.MTS_UPPERCASE)) && ((str.contains(Constants.DATACARD_LOWCASE) || str.contains(Constants.DATACARD_UPPERCASE)))){
//            // initiate MTS datacard bill
//        }else{
//            response = errRandomMessages();
//        }
//
//        return response;
//    }
//
//    private String errRandomMessages(){
//        return (Math.random() < 0.5) ? Constants.errorMsgs[0] : Constants.errorMsgs[1];
//    }
//
//
//    public ResponseObj makePayment(String str, String input){
//        ResponseObj obj = new ResponseObj();
//        obj.setResult(false);
//        String response = "";
//        if(input.equalsIgnoreCase("Yes")){
//            // do payment
//        }else if(input.equalsIgnoreCase("No")){
//            // restart
//            obj.setResponse(correctRandomMessages());
//            obj.setResult(false);
//        }else{
//            obj.setResponse(errRandomMessages());
//            obj.setResult(false);
//        }
//        return obj;
//    }
}
