import java.util.Scanner;

public class Codeforces_1370A {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        int[] nums = new int[t];
        for (int i = 0; i < t; i++) {
            nums[i] = in.nextInt();
        }
        for (int i = 0; i < t; i++) {
            if (nums[i] % 2 != 0) nums[i]--;
            System.out.println(nums[i] / 2);
        }
    }
}
