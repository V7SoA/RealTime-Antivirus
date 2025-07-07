public class RealTimeAntivirus {
    public static void main(String[] args) {
        System.out.println("🚀 Starting Real-Time Antivirus Application...");

        try {
            AntivirusDashboardUI.launch(AntivirusDashboardUI.class, args);
        } catch (Exception e) {
            System.err.println("Error launching the application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
